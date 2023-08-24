package app.android.movieapp.ui.activity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import app.android.movieapp.R
import app.android.movieapp.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.lang.Exception

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleLauncher: ActivityResultLauncher<Intent>
    private lateinit var sharedPreferences: SharedPreferences

    private var emailValido: Boolean = false
    private var passwordValido: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = Firebase.auth
        googleLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == RESULT_OK){
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    authFirebaseWithGoogle(account.idToken)
                }catch (_: Exception){
                }
            }
        }
        sharedPreferences = this.getSharedPreferences(SESSION_PREFERENCES_KEY, MODE_PRIVATE)
        iniciarEventosLogin()
    }

    private fun iniciarEventosLogin(){
        binding.tilEmail.editText?.addTextChangedListener { text ->
            emailValido = validarEmail(text.toString())
            binding.btnLogin.isEnabled = emailValido && passwordValido
        }
        binding.tilPassword.editText?.addTextChangedListener { text ->
            passwordValido = validarPassword(text.toString())
            binding.btnLogin.isEnabled = emailValido && passwordValido
        }
        binding.btnLogin.setOnClickListener {
            val email = binding.tilEmail.editText?.text.toString()
            val password = binding.tilPassword.editText?.text.toString()
            signInWithFirebase(email, password)
        }
        binding.btnLoginGoogle.setOnClickListener {
            signinWithGoogle()
        }
        binding.btnRegister.setOnClickListener {
            irARegister()
        }
    }

    private fun signInWithFirebase(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if(task.isSuccessful){
                    val user = firebaseAuth.currentUser
                    with(sharedPreferences.edit()) {
                        putString(EMAIL_DATA, user?.email).commit()
                    }
                    irAMain()
                }else{
                    Toast.makeText(this, "Correo electrónico o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun signinWithGoogle() {
        val googleSigInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()
        val googleClient = GoogleSignIn.getClient(this, googleSigInOptions)
        val intent = googleClient.signInIntent
        googleLauncher.launch(intent)
    }

    private fun authFirebaseWithGoogle(idToken: String?) {
        val authCredential = GoogleAuthProvider.getCredential(idToken!!, null)
        firebaseAuth.signInWithCredential(authCredential)
            .addOnCompleteListener(this) {task ->
                if(task.isSuccessful){
                    val user = firebaseAuth.currentUser
                    with(sharedPreferences.edit()) {
                        putString(EMAIL_DATA, user?.email).commit()
                    }
                    irAMain()
                }else{
                    Toast.makeText(this, "Ocurrió un error", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun validarEmail(email: String): Boolean{
        return Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.isNotEmpty()
    }

    private fun validarPassword(password: String): Boolean{
        return password.length >= 6
    }

    private fun irARegister() {
        val intent = Intent(this, RegisterUserActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun irAMain(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        const val SESSION_PREFERENCES_KEY = "SESSION_PREFERENCES_KEY"
        const val EMAIL_DATA = "EMAIL_DATA"
    }
}