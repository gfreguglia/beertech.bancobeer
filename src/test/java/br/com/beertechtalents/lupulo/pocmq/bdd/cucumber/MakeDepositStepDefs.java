package br.com.beertechtalents.lupulo.pocmq.bdd.cucumber;

import br.com.beertechtalents.lupulo.pocmq.controller.dto.ConsultaSaldoDTO;
import br.com.beertechtalents.lupulo.pocmq.controller.dto.JwtResponse;
import br.com.beertechtalents.lupulo.pocmq.model.Operacao;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class MakeDepositStepDefs {

    @Autowired
    MockMvc mvc;
    private MvcResult mvcResult;
    private ObjectMapper objectMapper = new ObjectMapper();

    private static String contaUuid;
    private static String jwtToken;
    private static final String contaEmail = "deposit@deposit.lupulo";
    private static final String contaSenha = "123";
    private static BigDecimal currentSaldo;

    @When("client creates an account")
    public void clientMakesPOSTToConta() throws Exception {
        JSONObject object = new JSONObject();
        object.put("nome", "MockDeposit");
        object.put("email", contaEmail);
        object.put("cnpj", "12345678901234");
        object.put("senha", contaSenha);

        mvcResult = mvc.perform(post("/conta")
                .contentType(MediaType.APPLICATION_JSON)
                .content(object.toString()))
                .andReturn();
        contaUuid = mvcResult.getResponse().getContentAsString().replace("\"", "");
    }

    @When("client authenticates")
    public void clientMakesPOSTToAuthenticate() throws Exception {
        JSONObject object = new JSONObject();
        object.put("username", contaEmail);
        object.put("password", contaSenha);

        mvcResult = mvc.perform(post("/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(object.toString()))
                .andReturn();
        String authenticationResponse = mvcResult.getResponse().getContentAsString();
        JwtResponse jwtResponse = objectMapper.readValue(authenticationResponse, JwtResponse.class);
        jwtToken = jwtResponse.getToken();
    }

    @Given("client is logged in the account")
    public void clientIsLoggedInTheAccount() throws Exception {
        System.out.format("Current token is %s", jwtToken);
    }

    @When("client makes deposit of {int} moneys into his account")
    public void clientSendsDepositPOSTToOperacao(int arg0) throws Exception {
        JSONObject object = new JSONObject();
        object.put("conta", contaUuid);
        object.put("valor", arg0);
        object.put("tipo", Operacao.TipoTransacao.DEPOSITO);

        mvcResult = mvc.perform(post("/authenticate")
                .header("authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(object.toString()))
                .andReturn();

    }

    @When("client views his balance")
    public void clientSendsGETToContaUuidSaldo() throws Exception {
        String saldoURL = String.format("/conta/%s/saldo", contaUuid);
        mvcResult = mvc.perform(get(saldoURL)
                .header("authorization", "Bearer " + jwtToken))
                .andReturn();

        String saldoResponse = mvcResult.getResponse().getContentAsString();
        ConsultaSaldoDTO consultaSaldoDTO = objectMapper.readValue(saldoResponse, ConsultaSaldoDTO.class);
        currentSaldo = consultaSaldoDTO.getSaldo();
    }

    @And("current balance is {int}")
    public void currentBalanceIs(int arg0) {
        assertThat(currentSaldo).isEqualTo(BigDecimal.valueOf(arg0));
    }
}
