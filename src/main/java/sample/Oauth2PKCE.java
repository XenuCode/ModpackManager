package sample;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Random;

public class Oauth2PKCE {
    String  listOfScopes;
    String responseType;
    String client_id;
    String redirectUri;
    String state;
    String codeChallangeMethod;
    String codechallange;
    String codeVerifier;
    public Oauth2PKCE()
    {

    }
    public  String makePKCEAuthcodeRequest()
    {
        try {
            //https://github.com/login/oauth/authorize?response_type=code&client_id=0d58963889fd4d932e37&redirect_uri=http%3A%2F%2Fwww.wholesomecraft.pl%2Fapi%2Fauthentication%2FgithubOauth%2Fsucces.html&scope=repo&state=DFHIUY73
            state = generateState(10);
            codeVerifier = generateCodeVerifier();
            System.out.println("CODE VER: "+codeVerifier);
            codechallange = generateCodeChallange(codeVerifier);
            System.out.println("CODE CHALLANG: "+codechallange);
            System.out.println("https://github.com/login/oauth/authorize?response_type="+responseType+"&client_id="+client_id+"&redirect_uri="+redirectUri+"&scope="+listOfScopes+"&state="+state+"&code_challenge="+codechallange+"&code_challenge_method"+codeChallangeMethod);
            HttpGet request = new HttpGet("https://github.com/login/oauth/authorize?response_type="+responseType+"&client_id="+client_id+"&redirect_uri="+redirectUri+"&scope="+listOfScopes+"&state="+state+"&code_challenge="+codechallange+"&code_challenge_method"+codeChallangeMethod);

            final CloseableHttpClient httpClient = HttpClients.createDefault();
            // add request headers
            request.addHeader("custom-key", "mkyong");
            request.addHeader(HttpHeaders.USER_AGENT, "Googlebot");

            try (CloseableHttpResponse response = httpClient.execute(request)) {

                // Get HttpResponse Status
                System.out.println(response.getStatusLine().toString());

                HttpEntity entity = response.getEntity();
                Header headers = entity.getContentType();
                System.out.println(headers);

                if (entity != null) {
                    // return it as a String
                    String result = EntityUtils.toString(entity);
                    System.out.println(result);
                }

            }
            httpClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "https://github.com/login/oauth/authorize?response_type="+responseType+"&client_id="+client_id+"&redirect_uri="+redirectUri+"&scope="+listOfScopes+"&state="+state+"&code_challenge="+codechallange+"&code_challenge_method"+codeChallangeMethod;
    }
    private String generateState(int lenght)
    {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .limit(lenght)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
    private String generateCodeVerifier()
    {
        SecureRandom sr = new SecureRandom();
        byte[] code = new byte[32];
        sr.nextBytes(code);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(code);
    }
    private String generateCodeChallange(String codeVerifier) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        byte[] bytes = codeVerifier.getBytes("US-ASCII");
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(bytes, 0, bytes.length);
        byte[] digest = md.digest();
        return Base64.getUrlEncoder().encodeToString(digest);
    }

    public void setListOfScopes(String listOfScopes) {
        this.listOfScopes = listOfScopes;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public void setCodeChallangeMethod(String codeChallangeMethod) {
        this.codeChallangeMethod = codeChallangeMethod;
    }
}
