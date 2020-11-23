# Asennusohje

## Toiminta paikallisesti

Saadakseen sovelluksen pyörimään paikallisesti on tehtävä seuraavat asiat.
  1. Ladattava ohjelma omalle koneelle. Tämä onnistuu joko ...
  
   - ... [lataamalla zip-tiedosto](https://github.com/matiastamsi/lukuvinkkikirjasto/archive/main.zip). Huom. juurihakemiston nimeksi tulee lukuvinkkikirjasto-main tällä tavalla, joten kannattaa ottaa loppuosa "-main" pois eli nimeksi jää "lukuvinkkikirjasto".
   - ... kloonaamalla projekti komentorivin kautta komennolla:
    
            git clone git@github.com:matiastamsi/lukuvinkkikirjasto.git
            
  2. Menemällä hakemistoon: 
  
          /lukuvinkkikirjasto/Lukuvinkkikirjasto/
          
  3. Generoitava suoritettava ohjelma komennolla:
  
         gradle jar
         
  4. Siirryttävä hakemistoon, johon jar-tiedosto generoitiin:
  
          /lukuvinkkikirjasto/Lukuvinkkikirjasto/build/libs
          
  5. Ohjelman voi nyt suorittaa komennolla:
  
          java -jar Lukuvinkkikirjasto.jar
