## NNPIA semestrální práce - backend

Napsáno v spring boot.

Nasazena na platformě [heroku](https://nnpia.herokuapp.com/) s CleadDB Mysql databází.

Slouží jako REST API rozhranní pro React frontend aplikaci.

#### Téma
Tématem semestrální práce bylo napsání aplikace jež by mohla složit seniorům k zjednodušení jejich nakupování.
Cílem bylo vytvoření takové aplikace jež dovolí nepřihlášenému uživateli (senior) vytvoření nákupního seznamu, který
si pak bude moct vybrat některý z přihlášených uživatelů. Komunikace je předpokládána přes email. Z tohoto důvodu
také backend zasílá emailové zprávy seniorům, aby byli dostatečně informováni. Také v aplikaci je pak možné zjistit stav seznamu.

#### Spuštění
K lokálnímu spuštění je potřeba několik úkonů. Je nutné si vytvořit Mysql databázy a její konfiguraci napsat do souboru `application-local.properties`.
Dále je nutné zapnout flyway (enable=true) a v nastavení IDE nastavit profil na `local`. Pak již 
stačí jen zbuildovat mavení projekt.

Služby:

/public - dostupné bez autentizace, primárně slouží k vytváření nákupních seznamů

/token - slouží k loginu 
      
/api/cart - nutné s autentizací, k práci s nákupními seznamy

/api/user - nutné s autentizací,  k práci s uživateli