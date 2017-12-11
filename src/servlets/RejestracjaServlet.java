package servlets;

import dao.UzytkownicyDAO;
import models.Uzytkownik;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

@WebServlet("/rejestruj")
public class RejestracjaServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String login = request.getParameter("login");
        String haslo = request.getParameter("haslo");
        String haslo2 = request.getParameter("haslo2");
        String imie = request.getParameter("imie");
        String nazwisko = request.getParameter("nazwisko");
        String numerTelefonu = request.getParameter("numerTelefonu");
        String miejsceZamieszkania = request.getParameter("miejsceZamieszkania");
        String email = request.getParameter("email");

        String patternLogin = "[a-zA-Z0-9]{6,}";
        String patternHaslo = "^(?=.*[0-9]).{8,}$";
        String patternNumerTelefonu = "[0-9]{7,}";
        String patternEmail = "^[\\w!#$%&’*+/=\\-?^_`{|}~]+(\\.[\\w!#$%&’*+/=\\-?^_`{|}~]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
        String patternPusty = ".+";

        if (Pattern.matches(patternPusty, haslo) && Pattern.matches(patternPusty, login)
                && Pattern.matches(patternPusty, haslo2) && Pattern.matches(patternPusty, imie)
                && Pattern.matches(patternPusty, nazwisko) && Pattern.matches(patternPusty, numerTelefonu)
                && Pattern.matches(patternPusty, miejsceZamieszkania) && Pattern.matches(patternPusty, email)) {
            if (Pattern.matches(patternHaslo, haslo)) {
                if (Pattern.matches(patternLogin, login)) {
                    if (Pattern.matches(patternNumerTelefonu, numerTelefonu)) {
                        if (Pattern.matches(patternEmail, email)) {
                            if (haslo.equals(haslo2)) {
                                UzytkownicyDAO dao = new UzytkownicyDAO();
                                if (dao.isLogin(login)) {
                                    request.setAttribute("blad", "Ten login jest już zajęty!");
                                    doGet(request, response);
                                } else if (dao.isNumerTelefonu(Integer.parseInt(numerTelefonu))) {
                                    request.setAttribute("blad", "Ten numer telefonu jest już zajęty!");
                                    doGet(request, response);
                                } else if (dao.isEmail(email)) {
                                    request.setAttribute("blad", "Ten e-mail jest już zajęty!");
                                    doGet(request, response);
                                } else {
                                    Uzytkownik u = new Uzytkownik();
                                    u.setLogin(login);
                                    u.setHaslo(haslo);
                                    u.setImie(imie);
                                    u.setNazwisko(nazwisko);
                                    u.setNumerTelefonu(Integer.parseInt(numerTelefonu));
                                    u.setMiejsceZamieszkania(miejsceZamieszkania);
                                    u.setEmail(email);
                                    u.setRola("klient");
                                    if (dao.addUzytkownika(u))
                                        response.sendRedirect(request.getContextPath() + "/stronaGlowna");
                                    else
                                        request.setAttribute("blad", "Nie udało się zakończyć rejestracji!");
                                }
                            } else {
                                request.setAttribute("blad", "Hasła są różne od siebie!");
                                doGet(request, response);
                            }
                        } else {
                            request.setAttribute("blad", "Błędny adres e-mail!");
                            doGet(request, response);
                        }
                    } else {
                        request.setAttribute("blad", "Błędny numer telefonu! Długość minimum 7 cyfr");
                        doGet(request, response);
                    }
                } else {
                    request.setAttribute("blad", "Błędny login! Długość minimum 6 znaków (brak znaków szczególnych)");
                    doGet(request, response);
                }
            } else {
                request.setAttribute("blad", "Błędne hasło! Długość minimum 8 znaków (przynajmniej jedna cyfra)!");
                doGet(request, response);
            }
        } else {
            request.setAttribute("blad", "Wypełnij wszystkie pola!");
            doGet(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("WEB-INF/views/rejestracja.jsp").forward(request, response);
    }


}