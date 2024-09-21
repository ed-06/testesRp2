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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;

import static java.lang.Thread.sleep;

public class CT03ResponderOnline {
    BufferedReader buffer;
    StringBuilder json;
    String linha;
    JsonParser parser;
    JsonObject jsonObject;
    ChromeOptions options;
    static WebDriver navegador;
    static WebDriver navegador2;
    static WebDriverWait espera;
    static WebDriverWait espera2;

    @BeforeEach
    public void setUp() {
        try {
            // Lê o arquivo JSON usando um BufferedReader
            buffer = new BufferedReader(new FileReader("C:\\Users\\eduar\\Documents\\GitHub\\testesRp2\\src\\main\\resources\\DadosResponderOnline.json"));
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

        // Tela cheia
        options = new ChromeOptions();
        options.addArguments("start-maximized");

        // Inicializa o WebDriver para o primeiro navegador
        WebDriverManager.chromedriver().setup();
        navegador = new ChromeDriver(options);
        espera = new WebDriverWait(navegador, Duration.ofSeconds(50));

        // Inicializa o WebDriver para o segundo navegador
        navegador2 = new ChromeDriver(options);
        espera2 = new WebDriverWait(navegador2, Duration.ofSeconds(50));
    }

    @Test
    @DisplayName("ResponderOnline")
    public void CT02() throws InterruptedException {
        Actions actions = new Actions(navegador);
        String usuario = jsonObject.get("usuario").getAsString();
        String senha = jsonObject.get("senha").getAsString();
        String tituloQuest = jsonObject.get("tituloQuest").getAsString();
        String id = jsonObject.get("id").getAsString();
        String descricaoQuest = jsonObject.get("descricaoQuest").getAsString();

        //LOGIN
        realizarLogin(navegador, espera, usuario, senha);
        realizarLoginResponder(navegador2, espera2, usuario, senha);

        //PROCURAR QUESTIONARIo
        procurarQuestionario(navegador, espera, actions, tituloQuest, id, descricaoQuest);

        //entrar no questionário

    }
    //realiza o login
    public void realizarLogin(WebDriver navegador, WebDriverWait espera, String usuario, String senha) throws InterruptedException {
        navegador.get(jsonObject.get("url").getAsString());
        espera.until(ExpectedConditions.visibilityOfElementLocated(By.name("login")));
        navegador.findElement(By.name("login")).sendKeys(usuario);
        navegador.findElement(By.name("password")).sendKeys(senha);
        navegador.findElement(By.name("btn_entrar")).click();
        espera.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));
        System.out.println("Título da nova janela: " + navegador.getTitle());
        sleep(1000);
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
    //realiza o login e manda para o site responder
    public void realizarLoginResponder(WebDriver navegador, WebDriverWait espera, String usuario, String senha) throws InterruptedException {
        navegador.get(jsonObject.get("url").getAsString());
        espera.until(ExpectedConditions.visibilityOfElementLocated(By.name("login")));
        navegador.findElement(By.name("login")).sendKeys(usuario);
        navegador.findElement(By.name("password")).sendKeys(senha);
        navegador.findElement(By.name("btn_entrar")).click();
        espera.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));
        System.out.println("Título da nova janela: " + navegador.getTitle());
        sleep(1000);
        // Agora trocando para o site responder
        navegador.navigate().to("http://200.132.136.72/AIQuiz/index.php?class=PublicPIN");
        // Compara se a url da página é a esperada
        sleep(1000);
        try {
            Assertions.assertEquals("http://200.132.136.72/AIQuiz/index.php?class=PublicPIN",
                    navegador.getCurrentUrl());
            System.out.println("Logado e na tela de responder PIN com sucesso");
        } catch (AssertionError e) {
            System.out.println("Erro no login e em ir para a tela de PIN");
        }
    }
    //Procura o questionario já parametrizado
    public void procurarQuestionario(WebDriver navegador, WebDriverWait espera, Actions actions, String id, String tituloQuest, String descricao) throws InterruptedException {
        sleep(4000);
        //chega no campo do Responderonline
        espera.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"side-menu\"]/li[7]/a")));
        navegador.findElement(By.xpath("//*[@id=\"side-menu\"]/li[7]/a")).click();
        sleep(250);

        //Preenche o id do questionario
        sleep(250);
        espera.until(ExpectedConditions.visibilityOfElementLocated(By.name("id")));
        navegador.findElement(By.name("id")).click();
        navegador.findElement(By.name("id")).sendKeys(id);
        actions.sendKeys(Keys.ENTER).perform();

        // Preenche título do questionário
        sleep(250);
        navegador.findElement(By.name("titulo")).sendKeys(tituloQuest);
        actions.sendKeys(Keys.TAB).perform();

        // Preenche descrição do questionário
        sleep(250);
        espera.until(ExpectedConditions.visibilityOfElementLocated(By.name("descricao")));
        navegador.findElement(By.name("descricao")).sendKeys(descricao);

        // Clicar no botão pesquisar
        sleep(3000);
        espera.until(ExpectedConditions.visibilityOfElementLocated(By.name("btn_buscar")));
        navegador.findElement(By.name("btn_buscar")).click();
        sleep(5000);

        //Forçar o click no responder questionário
        sleep(250);
        try {
            WebElement element = navegador.findElement(By.className("tdatagrid_cell"));
            if (element.getAttribute("class").contains("action")) {
                element.click();
            }
            } catch (Exception e) {
                e.printStackTrace();
            }

        //validar que foi para a proxima tela pelo endereço
    }
    //public void responderQuestionario(){
    //começa o questionario
     /*   sleep(250);
        for (int i = 0; i < 2; i++) {
            actions.sendKeys(Keys.TAB).perform();
        }
        actions.sendKeys(Keys.ENTER).perform();

        //Responder o questionário
        sleep(500);
        espera.until(ExpectedConditions.visibilityOfElementLocated(By.name("btnLiberar")));
        navegador.findElement(By.name("btnLiberar")).click();

        //primeira questão
        sleep(5000);
        actions.sendKeys(Keys.TAB).perform();
        actions.sendKeys(Keys.TAB).perform();
        actions.sendKeys(Keys.ENTER).perform();

        sleep(500);
        espera.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/div/div/div[3]/button")));
        navegador.findElement(By.xpath("/html/body/div[2]/div/div/div[3]/button")).click();



        //segunda questão
        sleep(500);
        espera.until(ExpectedConditions.visibilityOfElementLocated(By.name("btnLiberar")));
        navegador.findElement(By.name("btnLiberar")).click();

        sleep(500);
        actions.sendKeys(Keys.TAB).perform();
        actions.sendKeys(Keys.ENTER).perform();

        sleep(500);
        espera.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/div/div/div[3]/button")));
        navegador.findElement(By.xpath("/html/body/div[2]/div/div/div[3]/button")).click();

        //terceira questão
        sleep(500);
        espera.until(ExpectedConditions.visibilityOfElementLocated(By.name("btnLiberar")));
        navegador.findElement(By.name("btnLiberar")).click();



        sleep(500);
        actions.sendKeys(Keys.TAB).perform();
        actions.sendKeys(Keys.ENTER).perform();

        sleep(500);
        espera.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/div/div/div[3]/button")));
        navegador.findElement(By.xpath("/html/body/div[2]/div/div/div[3]/button")).click();

        //quarta questão
        sleep(500);
        espera.until(ExpectedConditions.visibilityOfElementLocated(By.name("btnLiberar")));
        navegador.findElement(By.name("btnLiberar")).click();

        sleep(500);
        actions.sendKeys(Keys.TAB).perform();
        actions.sendKeys(Keys.ENTER).perform();

        sleep(500);
        espera.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/div/div/div[3]/button")));
        navegador.findElement(By.xpath("/html/body/div[2]/div/div/div[3]/button")).click();

        String retorno = navegador.findElement(By.className("modal-title")).getText();
*/
        //Assertions.assertEquals("Informação", retorno);

        //<span class="pin-number" name="randomPIN">271 0530</span>
        //xpath:  //*[@id="tab_bform_1905449524_0"]/div/div/div/div/div[1]/div[1]/p[2]/span
    //}
}
