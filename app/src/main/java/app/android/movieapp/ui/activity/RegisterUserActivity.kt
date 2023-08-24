package app.android.movieapp.ui.activity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import app.android.movieapp.databinding.ActivityRegisterUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterUserBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    private var emailValido: Boolean = false
    private var passwordValido: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = Firebase.auth

        sharedPreferences = this.getSharedPreferences(LoginActivity.EMAIL_DATA, MODE_PRIVATE)

        binding.tilEmail.editText?.addTextChangedListener { text ->
            emailValido = validarEmail(text.toString())
            binding.btnRegister.isEnabled = emailValido && passwordValido
        }
        binding.tilPassword.editText?.addTextChangedListener { text ->
            passwordValido = validarPassword(text.toString())
            binding.btnRegister.isEnabled = emailValido && passwordValido
        }
        binding.btnRegister.setOnClickListener {
            val email = binding.tilEmail.editText?.text.toString()
            val password = binding.tilPassword.editText?.text.toString()
            signUpWithFirebase(email, password)
        }
    }

    private fun signUpWithFirebase(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if(task.isSuccessful){
                    val user = firebaseAuth.currentUser
                    with(sharedPreferences.edit()) {
                        putString(LoginActivity.EMAIL_DATA, user?.email).commit()
                    }
                    Toast.makeText(this, "El usuario fue creado correctamente", Toast.LENGTH_SHORT).show()
                    Thread.sleep(1000)
                    irAMain()
                }else{
                    Toast.makeText(this, "El usuario no fue creado", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun validarEmail(email: String): Boolean{
        return Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.isNotEmpty()
    }

    private fun validarPassword(password: String): Boolean{
        return password.length >= 6
    }

    private fun irAMain(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}