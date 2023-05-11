package com.example.f1;

public class ImagenesCircuitos {
    static String getImage(String carrera){
        String url = "https://e00-elmundo.uecdn.es/elmundodeporte/especiales/2005/03/formula1/fotos/alonso/img/modelo.jpg";
        switch (carrera){
            case "Bahrain Grand Prix":
                url = "https://e00-marca.uecdn.es/deporte/motor/formula1/img/circuitos/bahrein.jpg";
                break;
            case "Saudi Arabian Grand Prix":
                url = "https://e00-marca.uecdn.es/deporte/motor/formula1/img/circuitos/arabia-saudi.jpg";
                break;
            case "Australian Grand Prix":
                url = "https://e00-marca.uecdn.es/deporte/motor/formula1/img/circuitos/australia.jpg";
                break;
            case "Azerbaijan Grand Prix":
                url = "https://e00-marca.uecdn.es/deporte/motor/formula1/img/circuitos/azerbaiyan.jpg";
                break;
            case "Miami Grand Prix":
                url = "https://e00-marca.uecdn.es/deporte/motor/formula1/img/circuitos/miami.jpg";
                break;
            case "Emilia Romagna Grand Prix":
                url = "https://e00-marca.uecdn.es/deporte/motor/formula1/img/circuitos/emilia-romagna.jpg";
                break;
            case "Monaco Grand Prix":
                url = "https://e00-marca.uecdn.es/deporte/motor/formula1/img/circuitos/monaco.jpg";
                break;
            case "Spanish Grand Prix":
                url = "https://e00-marca.uecdn.es/deporte/motor/formula1/img/circuitos/espana.jpg";
                break;
            case "Canadian Grand Prix":
                url = "https://e00-marca.uecdn.es/deporte/motor/formula1/img/circuitos/canada.jpg";
                break;
            case "Austrian Grand Prix":
                url = "https://e00-marca.uecdn.es/deporte/motor/formula1/img/circuitos/austria.jpg";
                break;
            case "British Grand Prix":
                url = "https://e00-marca.uecdn.es/deporte/motor/formula1/img/circuitos/gran-bretana.jpg";
                break;
            case "Hungarian Grand Prix":
                url = "https://e00-marca.uecdn.es/deporte/motor/formula1/img/circuitos/hungria.jpg";
                break;
            case "Belgian Grand Prix":
                url = "https://e00-marca.uecdn.es/deporte/motor/formula1/img/circuitos/belgica.jpg";
                break;
            case "Dutch Grand Prix":
                url = "https://e00-marca.uecdn.es/deporte/motor/formula1/img/circuitos/paises-bajos.jpg";
                break;
            case "Italian Grand Prix":
                url = "https://e00-marca.uecdn.es/deporte/motor/formula1/img/circuitos/italia.jpg";
                break;
            case "Singapore Grand Prix":
                url = "https://e00-marca.uecdn.es/deporte/motor/formula1/img/circuitos/singapur.jpg";
                break;
            case "Japanese Grand Prix":
                url = "https://e00-marca.uecdn.es/deporte/motor/formula1/img/circuitos/japon.jpg";
                break;
            case "Qatar Grand Prix":
                url = "https://e00-marca.uecdn.es/deporte/motor/formula1/img/circuitos/qatar.jpg";
                break;
            case "United States Grand Prix":
                url = "https://e00-marca.uecdn.es/deporte/motor/formula1/img/circuitos/eeuu.jpg";
                break;
            case "Mexico City Grand Prix":
                url = "https://e00-marca.uecdn.es/deporte/motor/formula1/img/circuitos/mexico.jpg";
                break;
            case "São Paulo Grand Prix":
                url = "https://e00-marca.uecdn.es/deporte/motor/formula1/img/circuitos/brasil.jpg";
                break;
            case "Las Vegas Grand Prix":
                url = "https://e00-marca.uecdn.es/deporte/motor/formula1/img/circuitos/las-vegas.jpg";
                break;
            case "Abu Dhabi Grand Prix":
                url = "https://e00-marca.uecdn.es/deporte/motor/formula1/img/circuitos/abu-dhabi.jpg";
                break;
        }
        return url;
    }
}
