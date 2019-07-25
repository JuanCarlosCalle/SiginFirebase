package com.example.siginfirebase

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInOptionsExtension
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApi
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_main.*
import java.sql.ClientInfoStatus
import kotlin.math.log
import com.example.siginfirebase.R.id as id1

class MainActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {
    override fun onConnectionFailed(p0: ConnectionResult) {
        Toast.makeText(this,""+p0.errorMessage,Toast.LENGTH_SHORT).show()


        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object{
        private val PERMISSION_CODE = 9999

    }

    lateinit var mGoogleApiClient:GoogleApiClient
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var alertDialog: AlertDialog

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode== PERMISSION_CODE)
        {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess)
            {
                val account=result.signInAccount
                val idToken=account!!.idToken

                val credential=GoogleAuthProvider.getCredential(idToken,null)
                firebaseAuthWhitGoogle(credential)
            }
            else
            {
                Log.d("EDMT_ERROR","Login no exitoso")
                Toast.makeText(this,"Login no exitoso",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWhitGoogle(credential: AuthCredential?) {
        firebaseAuth!!.signInWithCredential(credential!!)
            .addOnSuccessListener {authResult->
                val logged_email = authResult.user.email
                val logged_activity=Intent(this@MainActivity,LoggedActivity::class.java)
                logged_activity.putExtra("email",logged_email)
                startActivity(logged_activity)
            }
            .addOnFailureListener {
                e-> Toast.makeText(this,""+e.message,Toast.LENGTH_SHORT).show()

            }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        configureGoogleClient()

        firebaseAuth=FirebaseAuth.getInstance()

        alertDialog = SpotsDialog.Builder()
            .setContext(this)
            .setMessage("Porfavor espere")
            .setCancelable(false)
            .build()

        btn_sign_in.setOnClickListener {
            signIn()
        }




    }
    private  fun signIn(){
        val intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        startActivityForResult(intent, PERMISSION_CODE)
    }

    private fun configureGoogleClient(){
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleApiClient=GoogleApiClient.Builder(this)
            .enableAutoManage(this,this)
            .addApi(Auth.GOOGLE_SIGN_IN_API,options)
            .build()
        mGoogleApiClient.connect()
    }
}
