import os
import json
import time
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from webdriver_manager.chrome import ChromeDriverManager
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

# carregar json
# carregar json
current_dir = os.path.dirname(os.path.abspath(__file__))
json_path = os.path.join(current_dir, 'teste1.json')
with open(json_path) as f:
    credenciais = json.load(f)


urlPlataforma = credenciais["url"]
usuario = credenciais["usuario"]
senhha = credenciais["senha"]

# chromedriver config
driver = webdriver.Chrome(service=Service(ChromeDriverManager().install()))

# entra no aiquiz
driver.get(urlPlataforma)


time.sleep(1)

# login com json
WebDriverWait(driver, 10).until(EC.presence_of_element_located((By.NAME, "login")))
driver.find_element(By.NAME, "login").send_keys(usuario)
driver.find_element(By.NAME, "password").send_keys(senha)


time.sleep(1)

# clicar no botão de login
try:
    login_button = WebDriverWait(driver, 10).until(
        EC.element_to_be_clickable((By.ID, 'tbutton_btn_entrar'))
    )
    login_button.click()
    print("Login realizado com sucesso!")
except:
    print("Falha ao clicar no botão de login.")

time.sleep(1)