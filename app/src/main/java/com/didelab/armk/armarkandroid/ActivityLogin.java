package com.didelab.armk.armarkandroid;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.didelab.armk.armarkandroid.util.ArmarkTypefaceSpan;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;


/**
 * A login screen that offers login via email/password.
 */
public class ActivityLogin extends AppCompatActivity {

    //Id to identity READ_CONTACTS permission request.
    private static final int REQUEST_READ_CONTACTS = 0;

    private final String TAG = getClass().getSimpleName();
    private final String URL_WEB_API_USUARIOS = "http://wsarmark.azurewebsites.net/Api/Usuarios";

    // UI references.
    private AutoCompleteTextView edtLoginCorreo;
    private EditText edtLoginContrasena;
    private View mProgressView;
    private View mLoginFormView;

    private Context context;
    private SharedPreferences prefs;

    // Keep track of the login task to ensure we can cancel it if requested.
    private UserLoginTask mAuthTask = null;

    // Facebook
    private LoginButton btnLoginFacebook;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicializa Facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        // Configuración interfaz
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_activity_login);
        context = this;

        // Facebook
        AppEventsLogger.activateApp(this);

        // Preferencias
        prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);

        // Configuración del tipo de letra
        Typeface face = Typeface.createFromAsset(getAssets(), "font/vonique_regular.otf");
        TypefaceSpan typefaceSpan = new ArmarkTypefaceSpan(face);

        SpannableString spanStrHintContrasena = new SpannableString(getString(R.string.tag_login_contrasena));
        spanStrHintContrasena.setSpan(typefaceSpan, 0, spanStrHintContrasena.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        SpannableString spanStrHintCorreo = new SpannableString(getString(R.string.tag_login_correo));
        spanStrHintCorreo.setSpan(typefaceSpan, 0, spanStrHintCorreo.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        SpannableString spanStrBtnEntrar = new SpannableString(getString(R.string.tag_login_entrar));
        spanStrBtnEntrar.setSpan(typefaceSpan, 0, spanStrBtnEntrar.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        // Configuración de widgets
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        edtLoginCorreo = (AutoCompleteTextView) findViewById(R.id.edt_login_correo);
        edtLoginCorreo.setHint(spanStrHintCorreo);

        edtLoginContrasena = (EditText) findViewById(R.id.edt_login_contrasena);
        edtLoginContrasena.setHint(spanStrHintContrasena);
        edtLoginContrasena.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button btnLogin = (Button) findViewById(R.id.btn_login_entrar);
        btnLogin.setText(spanStrBtnEntrar);
        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                    attemptLogin();
                }

        });

        //  Configuración del botón de facebook
        btnLoginFacebook = (LoginButton) findViewById(R.id.btn_login_facebook);
        btnLoginFacebook.setReadPermissions("user_friends");

        // Registro del Callback de Facebook
        btnLoginFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.d(TAG, "registerCallback onSuccess");

                Log.i(TAG, "User ID: "
                        + loginResult.getAccessToken().getUserId()
                        + "\n" +
                        "Auth Token: "
                        + loginResult.getAccessToken().getToken());

                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("user_id", loginResult.getAccessToken().getUserId().toString());
                editor.putString("auth_token", loginResult.getAccessToken().getToken().toString());
                editor.commit();

                ingresar();
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "registerCallback onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d(TAG, "registerCallback onError");

                String mensaje = "Error al entrar: No se puede iniciar sesion en Facebook. Compruebe la conexión a Internet o inténtelo más tarde.";
                Log.d(TAG, mensaje);
                Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show();
            }
        });


    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        if (edtLoginContrasena != null){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edtLoginContrasena.getWindowToken(), 0);
        }

        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        edtLoginCorreo.setError(null);
        edtLoginContrasena.setError(null);

        // Store values at the time of the login attempt.
        String correo = edtLoginCorreo.getText().toString();
        String contrasena = edtLoginContrasena.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(contrasena) && !isContrasenaValida(contrasena)) {
            edtLoginContrasena.setError(getString(R.string.error_invalid_password));
            focusView = edtLoginContrasena;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(correo)) {
            edtLoginCorreo.setError(getString(R.string.error_field_required));
            focusView = edtLoginCorreo;
            cancel = true;
        } else if (!isCorreoValido(correo)) {
            edtLoginCorreo.setError(getString(R.string.error_invalid_email));
            focusView = edtLoginCorreo;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(correo, contrasena);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isCorreoValido(String correo) {
        //TODO: Replace this with your own logic
        return correo.contains("@");
    }

    private boolean isContrasenaValida(String contrasena) {
        //TODO: Replace this with your own logic
        return contrasena.length() > 4;
    }

    /**
     * Llama a la actividad principal luego de haber validado las credenciales del usuario
     */
    private void ingresar() {
        Intent i = new Intent(context, ActivityPrincipal.class);
        startActivity(i);
        finish();
    }



    private void infoUsuario() {
        if (AccessToken.getCurrentAccessToken() != null) {
            String token = AccessToken.getCurrentAccessToken().getToken();
            String idUser = Profile.getCurrentProfile().getId();
            Log.d(TAG, "idUser: " + idUser);
            Log.d(TAG, "token: " + token);
            Log.d(TAG, "getName: " + Profile.getCurrentProfile().getName());
            Log.d(TAG, "getFirstName: " + Profile.getCurrentProfile().getFirstName());
            Log.d(TAG, "getLastName: " + Profile.getCurrentProfile().getLastName());
            Log.d(TAG, "getMiddleName: " + Profile.getCurrentProfile().getMiddleName());
            Log.d(TAG, "getProfilePictureUri: " + Profile.getCurrentProfile().getProfilePictureUri(20, 20));
            Log.d(TAG, "getLinkUri: " + Profile.getCurrentProfile().getLinkUri());

        } else {
            Log.d(TAG, " AccessToken es nulo ");
        }
    }



    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String userId = prefs.getString("user_id", "");
        String authToken = prefs.getString("auth_token", "");
        if (!userId.isEmpty() && !authToken.isEmpty()) {
            Log.d(TAG, "UserId: " + userId);
            Log.d(TAG, "authToken: " + authToken);
            ingresar();
        } else {
            Log.d(TAG, "No se ha iniciado sesión.");
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Facebook
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mCorreo;
        private final String mContrasena;
        private final String mUsuario;
        private final String strItemsIniciarSesion;
        private String mensajeError = "";

        UserLoginTask(String email, String password) {

            mUsuario = "";
            mCorreo = email;
            mContrasena = password;
            strItemsIniciarSesion = getItemsIniciarSesion(mUsuario, mCorreo, mContrasena);
            Log.d(TAG, strItemsIniciarSesion);
        }

        private String getItemsIniciarSesion(String mUsuario, String mCorreo, String mContrasena) {
            JSONObject iniciarSesion = new JSONObject();
            JSONObject cadena = new JSONObject();
            String resultado = "{}";
            try {
                cadena.put("Usuario", mUsuario);
                cadena.put("Correo", mCorreo);
                cadena.put("Password", mContrasena);
                iniciarSesion.put("IniciarSesion", cadena);
                resultado = iniciarSesion.toString();
                Log.d("Test", "Resultado: " + resultado);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return resultado;
        }


        @Override
        protected Boolean doInBackground(Void... params) {
            boolean resultado = false;
            return isIniciarSesion(URL_WEB_API_USUARIOS, strItemsIniciarSesion);

        }

        public boolean isIniciarSesion(String URL, String datos) {
            boolean resultado = false;
            String textoJson = rtaIniciarSesionWebApi(URL, datos);

            if (textoJson.isEmpty()) {
                return false;
            }

            try {
                JSONObject jObject = new JSONObject(textoJson);

                JSONObject jObjectIniciarSesion = jObject.getJSONObject("Respuesta");
                String estado = jObjectIniciarSesion.getString("Estado");
                Log.d(TAG, "Estado: " + estado);
                resultado = (estado.equalsIgnoreCase("OK")) ? true : false;

                if(resultado) {
                    JSONObject jObjectUsuario = jObjectIniciarSesion.getJSONObject("Usuario");
                    SharedPreferences.Editor editor = prefs.edit();

                    String[] itemsUsuario = getResources().getStringArray(R.array.usuario_array);
                    for (int i = 0; i < itemsUsuario.length; i++) {
                        editor.putString(itemsUsuario[i], jObjectUsuario.getString(itemsUsuario[i]));
                    }
                    editor.putString("user_id",jObjectUsuario.getString("Id"));
                    editor.putString("auth_token", "webapi");
                    editor.commit();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return resultado;
        }

        public String rtaIniciarSesionWebApi(String URL, String datos) {

            String resultado = "";
            HttpClient client = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(URL);

            try {
                StringEntity se = new StringEntity(datos);

                se.setContentType("application/json;charset=UTF-8");
                se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));
                httpPost.setEntity(se);

                HttpResponse httpresponse = client.execute(httpPost);
                try {
                    resultado = EntityUtils.toString(httpresponse.getEntity());
                    resultado = (resultado != null) ? resultado : "";
                    Log.i(TAG, resultado);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                mensajeError = "Verifique su conexión a Internet: E0001";
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                mensajeError = "Verifique su conexión a Internet: E0002";
            } catch (IOException e) {
                e.printStackTrace();
                mensajeError = "Verifique su conexión a Internet.";
            }
            return resultado;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (!mensajeError.isEmpty()) {
                Toast.makeText(context, mensajeError, Toast.LENGTH_LONG).show();
                return;
            }

            if (success) {
                ingresar();
            } else {
                Toast.makeText(context, "Correo o contraseña inválidos. Por favor vuelva a intentarlo.", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

