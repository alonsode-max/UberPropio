package com.example.easy_send.viewmodel

import android.app.Activity
import android.content.ContentValues.TAG
import android.util.Log
import androidx.credentials.GetCredentialRequest
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit
import com.google.firebase.auth.GoogleAuthProvider
import androidx.credentials.CredentialManager
import android.content.Context
import com.example.easy_send.model.Country
import com.google.i18n.phonenumbers.PhoneNumberUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import java.util.Locale
import javax.inject.Inject
import kotlin.getValue

sealed interface AuthState {
    data object Idle        : AuthState
    data object Loading     : AuthState
    data class CodeSent(val code: String, val token: PhoneAuthProvider.ForceResendingToken): AuthState
    data object Authenticated : AuthState
    data class  Error(val msg: String) : AuthState
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: FirebaseAuth
): ViewModel(){
    var authState: AuthState by mutableStateOf(AuthState.Idle)
        private set

    val currentUser: FirebaseUser?
        get() = auth.currentUser

    val isLoggedIn: Boolean
        get() = auth.currentUser != null

    private val phoneUtil by lazy { PhoneNumberUtil.getInstance() }

    val countries: StateFlow<List<Country>> = flow {
        emit(loadCountries())
    }.flowOn(Dispatchers.Default).stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())


    //Funciones del telefono

    fun login(phone:String, activity: Activity) {
        if(phone.isEmpty()){
            authState = AuthState.Error("El número no puede estar vacío")
            return
        }
        authState=AuthState.Loading

        val options= PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()
        try {
            PhoneAuthProvider.verifyPhoneNumber(options)
        } catch (e: Exception) {
            authState = AuthState.Error(e.localizedMessage ?: "Error al iniciar")
        }
    }

    fun verifySmsCode(smsCode: String) {
        val currentState = authState as? AuthState.CodeSent ?: return

        authState = AuthState.Loading

        viewModelScope.launch {
            try {
                val credential = PhoneAuthProvider.getCredential(currentState.code, smsCode)

                auth.signInWithCredential(credential).await()

                authState = AuthState.Authenticated
            } catch (e: Exception) {
                Log.e(TAG, "Error verificando código", e)
                authState = AuthState.Error(e.localizedMessage ?: "Código inválido")
            }
        }
    }

    private fun loadCountries(): List<Country> {
        return phoneUtil.supportedRegions.map { isoCode ->
            val locale = Locale("", isoCode)
            val dialCode = "+${phoneUtil.getCountryCodeForRegion(isoCode)}"
            val flag = isoCode.uppercase().map { char ->
                Character.codePointAt("$char", 0) + 0x1F1E6 - 65
            }.joinToString("") { String(Character.toChars(it)) }
            Country(isoCode = isoCode, countryCode = dialCode,
                countryName = locale.getDisplayCountry(Locale.getDefault()), flagEmoji = flag)
        }.sortedBy { it.countryName }
    }

    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            viewModelScope.launch {
                try {
                    auth.signInWithCredential(credential).await()
                    authState = AuthState.Authenticated
                } catch (e: Exception) {
                    authState = AuthState.Error("Auto-login fallido: ${e.localizedMessage}")
                }
            }
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Log.e(TAG, "Error en firebase", e)
            authState = AuthState.Error(e.localizedMessage ?: "Error de verificación")
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            authState= AuthState.CodeSent(verificationId,token)
        }
    }






    //Login de google
    private fun firebaseAuthWithGoogle(idToken: String) {
        authState = AuthState.Loading

        viewModelScope.launch {
            try {
                val credential = GoogleAuthProvider.getCredential(idToken, null)

                auth.signInWithCredential(credential).await()

                authState = AuthState.Authenticated

            } catch (e: Exception) {
                Log.e(TAG, "Error en firebaseAuthWithGoogle", e)
                authState = AuthState.Error(e.localizedMessage ?: "Error al iniciar sesión con Google")
            }
        }
    }

    fun loginWithGoogle(context: Context) {
        authState = AuthState.Loading

        viewModelScope.launch {
            try {
                val credentialManager = CredentialManager.create(context)

                val result = credentialManager.getCredential(
                    context = context,
                    request = request
                )

                handleSignIn(result.credential)

            } catch (e: Exception) {
                Log.e(TAG, "Error en Google Sign In", e)
                authState = AuthState.Error(e.localizedMessage ?: "Error al mostrar las cuentas de Google")
            }
        }
    }

    val googleIdOption = GetGoogleIdOption.Builder()
        .setServerClientId("666155087102-2m9ic2cc5jqfgptv409rj6nb0s27b6cu.apps.googleusercontent.com")
        .setFilterByAuthorizedAccounts(false)
        .build()

    val request = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    private fun handleSignIn(credential: Credential) {
        if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

            firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
        } else {
            Log.w(TAG, "Credential is not of type Google ID!")
        }
    }


    //Login con email
    fun loginEmail(email: String, password: String) {
        authState = AuthState.Loading
        viewModelScope.launch {
            authState = try {
                auth.signInWithEmailAndPassword(email, password).await()
                AuthState.Authenticated
            } catch (e: Exception) {
                AuthState.Error(e.localizedMessage ?: "Error al iniciar sesión")
            }
        }
    }

    fun register(email: String, password: String) {
        authState = AuthState.Loading
        viewModelScope.launch {
            authState = try {
                auth.createUserWithEmailAndPassword(email, password).await()
                AuthState.Authenticated
            } catch (e: Exception) {
                AuthState.Error(e.localizedMessage ?: "Error al registrar")
            }
        }
    }


    fun logout() {
        auth.signOut()
        authState = AuthState.Idle
    }

    fun clearError() { authState = AuthState.Idle }
}
