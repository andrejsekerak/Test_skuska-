
import Obchod.Predajna;
import org.junit.jupiter.api.Test;
import pozemok.Pivnica;
import pozemok.Zahrada;
import rastliny.*;
import vyrobca.Vyrobca;

import java.lang.reflect.Constructor;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestZahrada {
    String tulipan = "tulipan";
    String ruza = "ruza";
    String levandula = "levandula";
    String dub = "dub";
    String javor = "javor";
    String borovica = "borovica";

    Vyrobca v = new Vyrobca();
    Predajna p = new Predajna();
    @Test
    public void testEqualsKvety() {
        Kvietky k1 = v.vytvorKvietok(levandula);
        Kvietky k2 = v.vytvorKvietok(levandula);
        Kvietky k3 = v.vytvorKvietok(ruza);
        assertEquals(k1, k2);
        assertNotEquals(k1, k3);
    }

    @Test
    public void testEqualsStromy() {

        Stromy s1 =  v.vytvorStrom("dub", 1.0);
        Stromy s2 = v.vytvorStrom("dub", 1.0);
        Stromy s3 = v.vytvorStrom("javor", 1.0);
        assertEquals(s1, s2);
        assertNotEquals(s1, s3);
    }

    @Test
    public void testPresadzaniePoradieNezalezi() {

        Zahrada a = new Zahrada();
        Zahrada b = new Zahrada();
        a.vysad(v.vytvorKvietok(tulipan));
        a.vysad(v.vytvorKvietok(ruza));
        a.vysad(v.vytvorKvietok(levandula));
        a.vysad(v.vytvorStrom(dub, 1.0));
        a.vysad(v.vytvorStrom(javor, 1.0));
        a.presad(b, 3, 2);
        List<Rastlina> vysadene = b.getVysadene();
        assertEquals(5, vysadene.size());
    }

    @Test
    public void testPivnicaViacnasobneOdoberanie() {
        Pivnica p = new Pivnica();
        p.pridajKvietok(v.vytvorKvietok(tulipan));
        p.pridajKvietok(v.vytvorKvietok(levandula));
        p.pridajKvietok(v.vytvorKvietok(ruza));
        assertEquals("Tulipan", p.odoberKvietok().getNazov());
        assertEquals("Levandula", p.odoberKvietok().getNazov());
        assertEquals("Ruža", p.odoberKvietok().getNazov());
    }

    @Test
    public void testSadenieLenZPivnice() {
        Pivnica p = new Pivnica();
        Zahrada z = new Zahrada();
        p.pridajStrom(v.vytvorStrom(borovica, 2.0));
        Stromy s = p.odoberStrom();
        z.vysad(s);
        assertEquals(1, z.getVysadene().size());
    }

    @Test
    public void testZahradaViacnasobneSadenie() {
        Zahrada z = new Zahrada();
        for (int i = 0; i < 5; i++) z.vysad(v.vytvorKvietok(tulipan));
        for (int i = 0; i < 3; i++) z.vysad(v.vytvorKvietok(ruza));
        for (int i = 0; i < 2; i++) z.vysad(v.vytvorStrom(dub, 1.0));
        assertEquals(10, z.getVysadene().size());
    }

    @Test
    public void testPresadzanieBezZmenyAkChybaJednaCast() {
        Zahrada a = new Zahrada();
        Zahrada b = new Zahrada();
        a.vysad(v.vytvorKvietok(tulipan));
        a.vysad(v.vytvorKvietok(ruza));
        assertThrows(IllegalArgumentException.class, () -> a.presad(b, 2, 1));
        //a.presad(b, 2, 1); // chyba strom
        assertEquals(2, a.getVysadene().size());
        assertEquals(0, b.getVysadene().size());
    }

    @Test
    public void testKupenyTulipanDoPivnicePotomSadeny() {
        Predajna obchod = new Predajna();
        Pivnica p = new Pivnica();
        Zahrada z = new Zahrada();
        Kvietky tulipan = obchod.predajKvietok("tulipan");
        p.pridajKvietok(tulipan);
        Kvietky odobrany = p.odoberKvietok();
        z.vysad(odobrany);
        assertEquals(1, z.getVysadene().size());
    }

    @Test
    public void testPoradieZachovanePriViacnasobnomSadeni() {
        Zahrada z = new Zahrada();
        Kvietky k1 = v.vytvorKvietok(tulipan);
        Kvietky k2 = v.vytvorKvietok(ruza);
        Kvietky k3 = v.vytvorKvietok(levandula);
        z.vysad(k1);
        z.vysad(k2);
        z.vysad(k3);
        List<Rastlina> rastliny = z.getVysadene();
        assertEquals(k1, rastliny.get(0));
        assertEquals(k2, rastliny.get(1));
        assertEquals(k3, rastliny.get(2));
    }

    @Test
    public void testZahradaSadbaAJPresunViacRastlin() {
        Zahrada a = new Zahrada();
        Zahrada b = new Zahrada();
        for (int i = 0; i < 3; i++) a.vysad(v.vytvorKvietok(tulipan));
        for (int i = 0; i < 2; i++) a.vysad(v.vytvorStrom(borovica, 2.0));
        a.presad(b, 3, 2);
        assertEquals(0, a.getVysadene().size());
        assertEquals(5, b.getVysadene().size());
    }

    @Test
    public void testRastlinyVPoliObsahujuKvetyAJStromy() {
        Zahrada z = new Zahrada();
        z.vysad(v.vytvorKvietok(tulipan));
        z.vysad(v.vytvorStrom(dub, 1.0));
        z.vysad(v.vytvorKvietok(levandula));
        z.vysad(v.vytvorStrom(borovica, 2.0));
        z.vysad(v.vytvorKvietok(ruza));
        List<Rastlina> rastliny = z.getVysadene();
        assertEquals(5, rastliny.size());
        assertTrue(rastliny.stream().anyMatch(r -> r instanceof Kvietky));
        assertTrue(rastliny.stream().anyMatch(r -> r instanceof Stromy));
    }
    @Test
    public void testPrazdnaPivnicaVratiNull() {
        Pivnica p = new Pivnica();
        assertNull(p.odoberKvietok());
        assertNull(p.odoberStrom());
    }

    @Test
    public void testStromOVysejAkoMaxHodiChybu() {
        assertThrows(IllegalArgumentException.class, () -> v.vytvorStrom(dub, 3.0));
        assertThrows(IllegalArgumentException.class, () -> v.vytvorStrom(javor, 2.0));
        assertThrows(IllegalArgumentException.class, () -> v.vytvorStrom(borovica, 5.0));
    }

    @Test
    public void testPresadzanieZachovaSpravnyPocet() {
        Zahrada a = new Zahrada();
        Zahrada b = new Zahrada();
        for (int i = 0; i < 2; i++) a.vysad(v.vytvorKvietok(tulipan));
        for (int i = 0; i < 2; i++) a.vysad(v.vytvorStrom(dub, 1.0));
        a.presad(b, 2, 2);
        assertEquals(4, b.getVysadene().size());
    }

    @Test
    public void testPivnicaFIFOZachovanieFarby() {
        Pivnica p = new Pivnica();
        p.pridajKvietok(v.vytvorKvietok(ruza));
        p.pridajKvietok(v.vytvorKvietok(levandula));
        Kvietky k1 = p.odoberKvietok();
        Kvietky k2 = p.odoberKvietok();
        assertEquals("Ruža", k1.getNazov());
        assertEquals("Levandula", k2.getNazov());
    }

    @Test
    public void testPivnicaStromyFIFOZachovanieVysky() {
        Pivnica p = new Pivnica();
        p.pridajStrom(v.vytvorStrom(dub, 1.0));
        p.pridajStrom(v.vytvorStrom(borovica, 2.0));
        Stromy s1 = p.odoberStrom();
        Stromy s2 = p.odoberStrom();
        assertEquals(1.0, s1.getVyska());
        assertEquals(2.0, s2.getVyska());
    }

    @Test
    public void testPresadzanieZohladnujeRovnost() {
        Zahrada a = new Zahrada();
        Zahrada b = new Zahrada();
        a.vysad(v.vytvorKvietok(tulipan)); //tul
        a.vysad(v.vytvorStrom(dub, 1.0)); //dub
        b.vysad(v.vytvorKvietok(tulipan)); //tul
        b.vysad(v.vytvorStrom(dub, 1)); //dub
        a.presad(b, 1, 1);
        assertEquals(4, b.getVysadene().size());
    }

    @Test
    public void testSadbaZPivniceAjStromyAjKvety() {
        Pivnica p = new Pivnica();
        Zahrada z = new Zahrada();
        p.pridajKvietok(v.vytvorKvietok(levandula)); //lev
        p.pridajStrom(v.vytvorStrom(dub, 1.0)); //dub
        z.vysad(p.odoberKvietok());
        z.vysad(p.odoberStrom());
        assertEquals(2, z.getVysadene().size());
    }

    @Test
    public void testSadeniePostupneViacnasobne() {
        Pivnica p = new Pivnica();
        Zahrada z = new Zahrada();
        for (int i = 0; i < 3; i++) p.pridajKvietok(v.vytvorKvietok(tulipan)); //tul
        for (int i = 0; i < 3; i++) z.vysad(p.odoberKvietok());
        assertEquals(3, z.getVysadene().size());
    }

    @Test
    public void testZahradaNemozePresaditViacAkoMa() {
        Zahrada a = new Zahrada();
        Zahrada b = new Zahrada();
        a.vysad(v.vytvorKvietok(tulipan));
        assertThrows(IllegalArgumentException.class, () -> a.presad(b, 2, 0));
    }

    @Test
    public void testPresadzanieZoZahradyDoPrazdnej() {
        Zahrada a = new Zahrada();
        Zahrada b = new Zahrada();
        a.vysad(v.vytvorKvietok(tulipan));
        a.vysad(v.vytvorStrom(dub, 1.0));
        a.presad(b, 1, 1);
        assertEquals(2, b.getVysadene().size());
        assertEquals(0, a.getVysadene().size());
    }

    @Test
    public void testPresadzanieNevymazeAkPocetNesedi() {
        Zahrada a = new Zahrada();
        Zahrada b = new Zahrada();
        a.vysad(v.vytvorKvietok(tulipan));
        a.vysad(v.vytvorKvietok(ruza));
        a.vysad(v.vytvorStrom(dub, 1.0));
        assertThrows(IllegalArgumentException.class, () -> a.presad(b, 5, 1)); // prilis vela kvetov
        assertEquals(3, a.getVysadene().size());
        assertEquals(0, b.getVysadene().size());
    }

    @Test
    public void testPresadzanieNevymazeAkZiadnyKvietok() {
        Zahrada a = new Zahrada();
        Zahrada b = new Zahrada();
        a.vysad(v.vytvorStrom(dub, 1.0));
        assertThrows(IllegalArgumentException.class, () -> a.presad(b, 1, 1));
        //a.presad(b, 1, 1);
        assertEquals(1, a.getVysadene().size());
        assertEquals(0, b.getVysadene().size());
    }
    @Test
    public void testPredajNeexistujucehoKvietka() {
        Predajna p = new Predajna();
        assertThrows(IllegalArgumentException.class, () -> p.predajKvietok("Konvalinky"));
    }

    @Test
    public void testPredajNeexistujucehoStromu() {
        Predajna p = new Predajna();
        assertThrows(IllegalArgumentException.class, () -> p.predajStrom("Ipel", 1.0));
    }

    @Test
    public void testKonstruktoryExistuju() throws Exception {
       /* Class<?>[] classes = {Tulipan.class, Ruza.class, Levandula.class, Dub.class, Javor.class, Borovica.class};
        for (Class<?> cls : classes) {
            Constructor<?> constructor = cls.getConstructors()[0];
            assertNotNull(constructor);
        }*/

        //todo pri testovani už finalnom si to odkomentuj
    }


}
