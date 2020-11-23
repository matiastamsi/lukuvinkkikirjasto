# Asennusohje

## Toiminta paikallisesti

Saadakseen sovelluksen pyörimään paikallisesti on tehtävä seuraavat asiat.
  1. Ladattava ohjelma omalle koneelle. Tämä onnistuu repositorion etusivulta _Clone or download_.
  2. Menemällä hakemistoon: 
  
          /lukuvinkkikirjasto/Lukuvinkkikirjasto/
          
  3. Generoitava suoritettava ohjelma komennolla:
  
         gradle jar
         
  4. Siirryttävä hakemistoon, johon jar-tiedosto generoitiin:
  
          /lukuvinkkikirjasto/Lukuvinkkikirjasto/build/libs
          
  5. Ohjelman voi nyt suorittaa komennolla:
  
          java -jar Lukuvinkkikirjasto.jar
