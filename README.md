# Speedrun Tracker
De Speedrun Tracker applicatie is een handige tool ontworpen voor speedrunners om hun voortgang en prestaties bij te houden tijdens het spelen van games. Of je nu een beginner bent of een ervaren speedrunner, deze app is ontwikkeld om het proces van het monitoren en analyseren van je speedruns te vergemakkelijken.

Met deze applicatie kan je dus een speedrun voor een game starten en beëindigen waarbij de tijd wordt berekent en deze te zien kan zijn met alle andere speedruns voor die game.
## Structuur
De applicatiestructuur ziet er als volgt uit:
![Speedrun](https://github.com/MichielDausy/EnterpriseDevExp/assets/91216885/ddce5ac4-3035-443f-9b37-eceb346a4e87)
## Features
De 3 belangrijkste features van de applicatie zijn de start/stop, leaderboard en API-gateway functionaliteiten
- Start en stop functionaliteit: Voor deze functionaliteit zijn er 2 endpoints gemaakt:
  - /start: Hier wordt er een gloednieuwe speedrun aangemaakt en wordt er een tijd bijgehouden die we de startijd noemen.
  - /end: een request naar deze endpoint zorgt ervoor dat de eindtijd bepaald word. Het tijdsverschil tussen de startijd en eindtijd is de effectieve tijdsduur van de speedrun.
- Top 5 leaderboard van games:
- API-gateway: 
## Technologieën gebruikt
- Spring boot
- MySQL
- MongoDB
- Junit
- Kubernetes
- Prometheus
- Grafana
