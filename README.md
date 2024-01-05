# Speedrun Tracker
De Speedrun Tracker applicatie is een handige tool ontworpen voor speedrunners om hun voortgang en prestaties bij te houden tijdens het spelen van games. Of je nu een beginner bent of een ervaren speedrunner, deze app is ontwikkeld om het proces van het monitoren en analyseren van je speedruns te vergemakkelijken.

Met deze applicatie kan je dus een speedrun voor een game starten en beëindigen waarbij de tijd wordt berekent en deze te zien kan zijn met alle andere speedruns voor die game.
## Structuur
De applicatiestructuur ziet er als volgt uit:

![Speedrun](https://github.com/MichielDausy/EnterpriseDevExp/assets/91216885/ddce5ac4-3035-443f-9b37-eceb346a4e87)

Je kan zien dat er 3 microservices en 1 api-gateway is.
Elke service heeft zowat dezelfde bestanden zoals controllers, dto's, models, repositories en services.

De controller is eigenlijk het toegangspunt tot de service, alle request die naar een service gaan komen eerst in de controller terecht. In zo'n controller staan alle endpoints van de service, als deze endpoints gebruikt worden zal de controller de service files gebruiken om de achterliggende logica te draaien.


In een service file wordt de logica dat moet gebeuren uitgevoerd alsook de communicatie tussen de microservices, bijvoorbeeld voor het deleeten van een game moet er eerst gecheckt worden of dat deze game niet meer in gebruik is voor een speedrun. Aangezien dat deze informatie niet in de game-service maar in de speedrun-service staat moet er een request gestuurd worden naar die service:

![image](https://github.com/MichielDausy/EnterpriseDevExp/assets/91216885/8c450b16-0640-4ff1-b4db-45bd071d94fa)

Om zowel data te ontvangen en te versturen worden dto's gebruikt.

Om data uit een database te halen worden repositories gebruikt. De service files gebruiken deze repositories om de nodige data te krijgen.

De 3 microservices die gebruikt worden in deze applicatie zijn:
- Game-service - MongoDB database
- Speedrun-service - MySQL database
- Profile-service - MySQL database
### Game-service
De game-service is op poort 8080 toegankelijk voor de andere services en de API-gateway. in deze service heb je deze endpoints:
- ***/all***: geeft alle games van de applicatie terug
![image](https://github.com/MichielDausy/EnterpriseDevExp/assets/91216885/b21eb708-8f09-48e6-bb32-1ce791c5c925)

- ***/getid***: wordt enkel door de speedrun-service aangeroepen als er een nieuwe speedrun aangemaakt word. Het stuurt voor de gekozen game de ID (niet primary key maar een aparte waarde) mee.
- ***/top5***: Dit is de belangrijkste endpoint voor deze service. voor een ingegeven game wordt er een request gestuurd naar de speedrun-service om de 5 beste speedruns van deze game te verzamelen. In de speedrun-service staat de logica om de top 5 speedruns te verkrijgen waarbij een request wordt gestuurd van speedrun-service naar de profile-service om ook de profiles erbij te kunnen zien
![image](https://github.com/MichielDausy/EnterpriseDevExp/assets/91216885/7e5a420f-9988-4cdc-b8de-26ec44d1c0a2)

- ***/create***: Maakt een nieuwe game aan, zonder authenticatie zal dit een 401 unauthorized error teruggeven
![image](https://github.com/MichielDausy/EnterpriseDevExp/assets/91216885/b7c3f0a3-49c7-43df-8b10-8acc45927ffa)

Als ik mezelf authoriseer dan lukt dit wel:
![image](https://github.com/MichielDausy/EnterpriseDevExp/assets/91216885/02db5548-18ad-443a-a4f6-cfed4c9253a8)

- ***/update***: Wijzigt een bestaande game
![image](https://github.com/MichielDausy/EnterpriseDevExp/assets/91216885/0afc1481-edef-4300-8f2c-cceb84326b16)

- ***/delete***: Verwijderd een bestaande game. Het wijzigen lukt dus enkel als er geen speedruns meer aan gekoppeld zijn
Als de game nog gebruikt wordt bij een speedrun wordt deze error getoond:
![image](https://github.com/MichielDausy/EnterpriseDevExp/assets/91216885/3ff90ac5-2987-4cf3-adde-6ad7c174dd02)

Als de game niet in gebruik is is dit de output:
![image](https://github.com/MichielDausy/EnterpriseDevExp/assets/91216885/ba781a3a-2ff2-47ac-aeb6-895615240545)

### Speedrun-service
De speedrun-service is op poort 8081 toegankelijk voor de andere services en de API-gateway. in deze service heb je deze endpoints:
- ***/start***: Hier wordt er een gloednieuwe speedrun aangemaakt en wordt er een tijd bijgehouden die we de startijd noemen. Bij het aanmaken van een speedrun wordt er een game naam en profiel meegegeven, deze naam herkent de speedrun-service niet omdat ze niet in de database van deze service zitten, enkel de ID's ervan. Daarom wordt er een request naar de game-service en profile-service gestuurd om deze ID's te krijgen.
![image](https://github.com/MichielDausy/EnterpriseDevExp/assets/91216885/dd65201f-4fbe-4684-ade4-b73b8bea7b51)

- ***/end***: Door de ID van de speedrun (dat getoond wordt tijdens het aanmaken van een speedrun) mee te geven zal deze speedrun eindigen. Een eindtijd zal bijgehouden worden en de tijdsduur tussen deze 2 is de uiteindelijke tijd waarin de game gespeeld is.
![image](https://github.com/MichielDausy/EnterpriseDevExp/assets/91216885/95cfdb1d-71cf-4f66-ada6-567153338d40)

- ***/profile***: Deze endpoint geeft alle speedruns van een bepaalde profiel
![image](https://github.com/MichielDausy/EnterpriseDevExp/assets/91216885/2130333d-d532-4798-a231-233fc818b617)

- ***/gameid***:  Als een game verwijderd moet worden dan gaat de game-service een request doen naar deze endpoint om te checken of dat de game niet meer in gebruik is voor een speedrun.
- ***/profileid***: Als een profiel verwijderd moet worden dan gaat de profile-service een request doe nnaar deze endpoint om te checken of dat de profiel niet meer in gebruik is voor een speedrun.
- ***/top5***: Deze wordt gebruikt door de game-service waarbij de 5 beste speedruns uit de database gehaald worden. Er wordt hier ook een request naar de profile-service gestuurd om voor de die 5 speedruns de profielen bij te tonen.
### Profile-service
De profile-service is op poort 8082 toegankelijk voor de andere services en de API-gateway. in deze service heb je deze endpoints:
- ***/all***: geeft alle profielen van de applicatie terug
![image](https://github.com/MichielDausy/EnterpriseDevExp/assets/91216885/03c3f811-385b-4dbd-b35d-71277aa573f8)

- ***/getid***: wordt enkel door de speedrun-service aangeroepen als er een nieuwe speedrun aangemaakt word. Het stuurt voor de gekozen profiel de ID (niet primary key maar een aparte waarde) mee.
- ***/top5***: Deze wordt gebruikt door de speedrun-service om de profielen te krijgen die bij de 5 beste speedruns toebehoren
- ***/create***: Maakt een nieuwe profiel aan
![image](https://github.com/MichielDausy/EnterpriseDevExp/assets/91216885/c456b49e-e288-4c1f-86ac-584075be0ae6)

- ***/update***: Wijzigt een bestaande profiel
![image](https://github.com/MichielDausy/EnterpriseDevExp/assets/91216885/1f4cfc17-875b-456a-be67-f47afee81b75)

- ***/delete***: Verwijderd een bestaande profiel. Het wijzigen lukt dus enkel als er geen speedruns meer aan gekoppeld zijn
![image](https://github.com/MichielDausy/EnterpriseDevExp/assets/91216885/84e35898-dace-42e2-ba20-43e813a06eba)

### API-gateway
De API-gateway wordt gebruikt om toegang tot de services van buiten de applicatie naar de juiste endpoints te sturen. Hierbij zijn sommige endpoints toegankelijk voor iedereen:
- ***/speedruns/profile***
- ***/games***
- ***/games/top5***

En sommige zijn enkel toegankelijk door te authenticeren met OAuth2:
- ***/speedruns/start***
- ***/speedruns/end***
- ***/games/create***
- ***/games/update***
- ***/games/delete***
- ***/profiles***
- ***/profiles/create***
- ***/profiles/update***
- ***/profiles/delete***
## 2.2 Kubernetes
Om de applicatie te deployen heb ik gekozen om Kubernetes te gebruiken. **Deployen naar Okteto is niet gelukt omdat ze geen nieuwe accounts meer toelaten**, dus heb ik de cluster lokaal moeten draaien m.b.v. Docker-desktop.

Alle files die te maken hebben met het in orde brengen van Kubernetes staan in de Kubernetes folder in deze repo. In deze folder zijn de verschillende soorten resources terug te vinden:

![image](https://github.com/MichielDausy/EnterpriseDevExp/assets/91216885/afdcb514-8c2a-4a0c-a779-1dbb0e526408)

Onder de deployments folder staan alle deployments die ik gebruik. Deze gaan pods maken van de microservices, de API-gateway, de databases en ook prometheus monitoring en Grafana.
Belangrijk is dat in de deployments de correcte docker images van docker hub worden gehaald. Deze images worden autolmatisch gepushed naar docker hub door de github actions in deze repo. Hierdoor zijn er 4 images op docker hub die ik gebruik om de pod aan te maken.

![image](https://github.com/MichielDausy/EnterpriseDevExp/assets/91216885/f93f5941-7322-4cba-9f3d-3056411f43ca)

Environment variabelen zijn ook gespecifieerd in de deployment files. De microservices verwachten een environment variabele dat aangeeft met welke database ze moeten communiceren en met welke url ze moeten communiceren naar de andere microservices. Als voorbeeld neem ik de speedrun microservice:

![image](https://github.com/MichielDausy/EnterpriseDevExp/assets/91216885/1067680c-597f-4903-9837-c889bff3087c)

Deze verwacht 4 variabelen:
- MYSQL_DB_HOST
- MYSQL_DB_PORT
- PROFILE_SERVICE_BASEURL
- GAME_SERVICE_BASEURL

Al deze variabelen worden dus in de deployment files meegegeven

![image](https://github.com/MichielDausy/EnterpriseDevExp/assets/91216885/86724cd5-6827-4fae-822b-62060128c20c)

Ik heb ook een ingress resource gemaakt zodat er van buiten de cluster een connectie met de API-gateway gemaakt kan worden.

![image](https://github.com/MichielDausy/EnterpriseDevExp/assets/91216885/0a2e9b6b-069f-474d-9a74-65bbfe09244e)

De DNS is speedrun.com (speedrun.com moet toegevoegd worden aan de hosts file) en vanaf dat je op de root zit en de juiste poort meegeeft ben je verbonden met de API-gateway. Om dit te laten werken heb ik een Nodeport van de API-gateway service meoten maken:

![image](https://github.com/MichielDausy/EnterpriseDevExp/assets/91216885/a56a69cb-9d97-4995-aff8-a3a79fb7020b)

Dit zodat ik op de juiste poort zal komen en met de API-gateway verbonden zal zijn.
## 2.2.1 Prometheus
Prometheus is een populaire monitoringtool voor Kubernetes vanwege de naadloze integratie met de Kubernetes-infrastructuur. Het begrijpt Kubernetes-componenten, verzamelt op efficiënte wijze statistieken en maakt flexibele query's voor monitoring mogelijk.

Om prometheus te laten werken heb ik terug een service, deployment en een persistent volume moeten maken alsook een configMap met de scrapejobs om de pods te ontdekken in de cluster.

Uiteindelijk kwam ik uit op de Prometheus Expression Browser dat PromQL gebruikt om query's uit te voeren voor de monitoring.

![image](https://github.com/MichielDausy/EnterpriseDevExp/assets/91216885/5f54bd62-b0c2-4ada-b63a-e0536c50bd25)

De query hierboven toont de status van de replicas van de deployments. Ik heb voor elke deployment maar 1 replica gemaakt maar als je de deployment aanpast en meerdere replicas wenst dan zullen de waardes rechts ook veranderen.

Nog een voorbeeld van een query is om te monitoren of dat de deployment "available" is

![image](https://github.com/MichielDausy/EnterpriseDevExp/assets/91216885/f3042eb6-7814-44bd-83c3-03bc94f2d758)

Er zijn hier voor elke deployment 3 rijen:
- Availability status = true
- Availability status = false
- Availability status = unknown

Ik kan ook bijvoorbeeld zien hoeveel storage elke volume heeft gekregen:

![image](https://github.com/MichielDausy/EnterpriseDevExp/assets/91216885/2fcabb63-0200-4394-b76d-58bb194317c8)

Zoals je kan zien is de volume ongeveer 100MB groot (de waarden staan in bytes)
## 2.2.1.1 Grafana
Ten slotte is er ook nog een dashboard gemaakt met Grafana om de monitoring iets makkelijker te interpreteren. Ook voor Grafana heb ik een persisten volume, service, deployment en 2 configMaps moeten maken. In deze configMaps staat de connectie met de prometheus service zodat Grafana de data uit prometheus kan halen om in de visualizaties te steken. En de 2de configMap staat de json code van hoe het dashboard eruit zal zien met alle juiste queries.

Het dashboard dat ik heb gemaakt ziet er als volgt uit:

![image](https://github.com/MichielDausy/EnterpriseDevExp/assets/91216885/3c49a4bd-8384-40ac-899c-ac6923771c65)
![image](https://github.com/MichielDausy/EnterpriseDevExp/assets/91216885/fffb4671-f705-43a0-85ce-0146bd5acd43)
![image](https://github.com/MichielDausy/EnterpriseDevExp/assets/91216885/056e6db7-f3c3-421f-a84d-b1548c702c94)

Er staan veel nuttige visualizaties in zoals:
- Totale CPU gebruik
- totale RAM gebruik
- Hoeveel pods er aan het draaien zijn
- CPU gebruik per pod
- RAM gebruik per pod
- Hoeveel pods een bepaalde status zoals "running" hebben
- Aantal containers dat op elke pod draaien
- Aantal replicas per deployment
- Of dat er replicas unavailable zijn
## Technologieën gebruikt
- Spring boot: Met dit framework en dependencies zoals Lombok, Spring Cloud Gateway, etc.
- MySQL: De RDMS voor 2 microservices + prometheus en Grafana storage
- MongoDB: De RDMS voor de game microservice
- OAuth2: Via Google Cloud Platform authenticatie toegevoegd aan de API-gateway
- Junit & Mockito: Gebruikt voor het testen van alle endpoints
- Kubernetes: Gebruikt om de applicatie te deployen
- Prometheus: Monitoring van de pods en cluster
- Grafana: Monitoring van de pods en cluster via visualisaties
