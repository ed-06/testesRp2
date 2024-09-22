import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.security.SecureRandom;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import static java.lang.Thread.sleep;

public class CT02_2RespostaVazio {
    BufferedReader buffer;
    StringBuilder json;
    String linha;
    JsonParser parser;
    JsonObject jsonObject;
    ChromeOptions options;
    static WebDriver navegador;
    static WebDriverWait espera;

    @BeforeEach
    public void setUp() {
        try {
            // Lê o arquivo JSON usando um BufferedReader
            buffer = new BufferedReader(new FileReader("C:\\Users\\eduar\\Documents\\GitHub\\testesRp2\\src\\main\\resources\\DadosQuestComChatGpt.json"));
            json = new StringBuilder();
            while ((linha = buffer.readLine()) != null) {
                json.append(linha);
            }
            buffer.close();

            // Converte o conteúdo do arquivo JSON em um objeto JsonObject
            parser = new JsonParser();
            jsonObject = parser.parse(json.toString()).getAsJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Define as opções do Chrome
        options = new ChromeOptions();
        options.addArguments("start-maximized");

        // Inicializa o WebDriver
        WebDriverManager.chromedriver().setup();
        navegador = new ChromeDriver(options);
        espera = new WebDriverWait(navegador, Duration.ofSeconds(50));
    }

    @Test
    @DisplayName("Questionario com chatGPT")
    public void CT01() throws InterruptedException {
        Actions actions = new Actions(navegador);
        SecureRandom RANDOM = new SecureRandom();
        String urlPlataforma = jsonObject.get("url").getAsString();
        String usuario = jsonObject.get("usuario").getAsString();
        String senha = jsonObject.get("senha").getAsString();
        String nomeQuest = jsonObject.get("nomeQuest").getAsString();
        String qtdPerguntas  = jsonObject.get("qtdPerguntas").getAsString();
        String qtdAlternativas = jsonObject.get("qtdAlternativas").getAsString();
        String tema = jsonObject.get("tema").getAsString();
        String disciplina = jsonObject.get("disciplina").getAsString();
        String resposta = jsonObject.get("resposta").getAsString();

        // Logar no site e chegar no menu chatGPT
        realizarLogin(navegador, espera, usuario, senha);
        //criar questionario com gpt
        criarQuestionario(navegador, espera, actions, nomeQuest, qtdPerguntas, qtdAlternativas, tema);
        //responder as questionario
        responderQuestionario(navegador, espera, actions, disciplina, resposta);

    }
    public void realizarLogin(WebDriver navegador, WebDriverWait espera, String usuario, String senha) throws InterruptedException {
        navegador.get(jsonObject.get("url").getAsString());
        espera.until(ExpectedConditions.visibilityOfElementLocated(By.name("login")));
        navegador.findElement(By.name("login")).sendKeys(usuario);
        navegador.findElement(By.name("password")).sendKeys(senha);
        navegador.findElement(By.name("btn_entrar")).click();
        espera.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));
        System.out.println("Título da nova janela: " + navegador.getTitle());
        sleep(1000);
        //chega no menu com chatgpt
        espera.until(d -> navegador.findElement(By.xpath("//*[@id=\"side-menu\"]/li[2]/a")));
        navegador.findElement(By.xpath("//*[@id=\"side-menu\"]/li[2]/a")).click();
        //Espera 2 segundos para verificar
        Thread.sleep(2000);
        // Compara se a url da página é a esperada
        try {
            Assertions.assertEquals("http://200.132.136.72/AIQuiz/index.php?class=ResponderListOnLine&previous_class=LoginForm",
                    navegador.getCurrentUrl());
            System.out.println("Logado com sucesso");
        } catch (AssertionError e) {
            System.out.println("Erro no login");
        }
    }
    public void criarQuestionario(WebDriver navegador, WebDriverWait espera, Actions actions, String nomeQuest, String qtdPerguntas
            , String qtdAlternativas, String tema) throws InterruptedException {
        espera.until(d -> navegador.findElement(By.name("questionario")));
        navegador.findElement(By.name("questionario")).click();

        //poe o nome no questionario
        navegador.findElement((By.name("questionario"))).clear();
        espera.until(d -> navegador.findElement(By.name("questionario")));
        navegador.findElement(By.name("questionario")).sendKeys(nomeQuest);

        // vai para o campo das perguntas
        sleep(500);
        actions.sendKeys(Keys.TAB).perform();

        //quantidade de perguntas
        espera.until(d -> navegador.findElement(By.name("qtdPerguntas")));
        navegador.findElement((By.name("qtdPerguntas"))).sendKeys(qtdPerguntas);

        //vai para o quantidade de alternaticas
        actions.sendKeys(Keys.TAB).perform();
        navegador.findElement((By.name("qtdAlternativas"))).clear();
        espera.until(d -> navegador.findElement(By.name("qtdAlternativas")));
        navegador.findElement((By.name("qtdAlternativas"))).sendKeys(qtdAlternativas);

        //chega no campo tema e limpa ele
        actions.sendKeys(Keys.TAB).perform();
        navegador.findElement((By.name("tema"))).clear();
        espera.until(d -> navegador.findElement(By.name("tema")));
        navegador.findElement((By.name("tema"))).sendKeys(tema);

        //elaborar perguntas
        sleep(500);
        espera.until(d -> navegador.findElement(By.name("btn_elabore_perguntas")));
        navegador.findElement(By.name("btn_elabore_perguntas")).click();

        //proximo
        sleep(4000);
        espera.until(d -> navegador.findElement(By.name("btn_próximo")));
        navegador.findElement(By.name("btn_próximo")).click();

        sleep(500);
        //Verefica a criação do quiz
        try {
            Assertions.assertEquals("http://200.132.136.72/AIQuiz/index.php?class=ResponderListOnLine&previous_class=LoginForm",
                    navegador.getCurrentUrl());
            System.out.println("Quiz criado da forma correta");
        } catch (AssertionError e) {
            System.out.println("Erro na criação do quiz");
        }
    }
    public void responderQuestionario(WebDriver navegador, WebDriverWait espera, Actions actions, String disciplina, String resposta) throws InterruptedException {
        //disciplina
        espera.until(d -> navegador.findElement(By.name("disciplina")));
        navegador.findElement(By.name("disciplina")).click();
        sleep(100);
        actions.sendKeys(disciplina).perform();
        sleep(100);
        actions.sendKeys(Keys.ENTER);

        sleep(100);
        actions.sendKeys(Keys.ENTER);

        //escolhas 1a
        espera.until(d -> navegador.findElement(By.name("radio1")));
        navegador.findElement(By.name("radio1")).click();
        //2a
        espera.until(d -> navegador.findElement(By.name("radio2")));
        navegador.findElement(By.name("radio2")).click();
        //3a
        espera.until(d -> navegador.findElement(By.name("radio3")));
        navegador.findElement(By.name("radio3")).click();
        //4b
        espera.until(d -> navegador.findElement(By.name("radio4")));
        navegador.findElement(By.name("radio4")).click();

        //confirma o questionario
        sleep(1000);
        espera.until(d -> navegador.findElement(By.name("btn_confirma")));
        navegador.findElement(By.name("btn_confirma")).click();
        sleep(2000);
        WebElement mensagemErro = navegador.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div/span[2]"));
        String textoMensagemErro = mensagemErro.getText();
        Assertions.assertEquals("O campo Tempo da Resposta é obrigatório.", mensagemErro.getText());
    }
}
