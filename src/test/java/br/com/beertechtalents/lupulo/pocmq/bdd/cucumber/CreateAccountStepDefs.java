package br.com.beertechtalents.lupulo.pocmq.bdd.cucumber;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class CreateAccountStepDefs {

    @Autowired
    MockMvc mvc;
    private MvcResult mvcResult;

    private static String contaUuid;

    @When("client makes POST to \\/conta")
    public void clientMakesPOSTToConta() throws Exception {
        JSONObject object = new JSONObject();
        object.put("nome", "Mock");
        object.put("email", "teste@teste.com.br");
        object.put("cnpj", "11111111111111");
        object.put("senha", "123");

        mvcResult = mvc.perform(post("/conta")
                .contentType(MediaType.APPLICATION_JSON)
                .content(object.toString()))
                .andReturn();
        contaUuid = mvcResult.getResponse().getContentAsString().replace("\"", "");
    }

    @When("client makes PATCH to \\/conta")
    public void clientMakesPATCHToConta() throws Exception {
        JSONObject object = new JSONObject();
        object.put("password", "321");
        mvcResult = mvc.perform(patch("/conta/" + contaUuid)
                .contentType(MediaType.APPLICATION_JSON)
                .content(object.toString()))
                .andReturn();
    }

    @Then("client receives response code {int}")
    public void clientReceivesResponseCode(int arg0) {
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(arg0);
    }

}
