import java.util.Scanner;
import java.util.InputMismatchException;

class Legesystem {
    private Scanner input; 
    private Liste<Lege> legeliste;
    private Liste<Pasient> pasientliste;
    private Liste<Legemiddel> legemiddelliste;
    private Liste<Resept> reseptliste;

    Legesystem()
    {
        this.input = new Scanner(System.in);
        this.legeliste = new SortertLenkeliste<Lege>();
        this.pasientliste = new Lenkeliste<Pasient>();
        this.reseptliste = new Lenkeliste<Resept>();
        this.legemiddelliste = new Lenkeliste<Legemiddel>();

        this.legeliste.leggTil(new Lege("Dr. Arne"));
        this.pasientliste.leggTil(new Pasient("Runar", "01010166644"));
        this.legemiddelliste.leggTil(new LegemiddelC("Morfin", 400, 400));
        this.reseptliste.leggTil(new HvitResept(legemiddelliste.hent(0),
                                           legeliste.hent(0), 
                                           pasientliste.hent(0), 
                                           5));

    }




    public void hovedMeny()
    {
        int valg = 0;


        while(true) {
            skrivUtHovedmeny();
            valg = hentValg();

            switch(valg)
            {
                case 0: 
                    return;
                case 1: 
                    skrivUtOversikt();
                    continue;
                case 2: 
                    leggTilMeny();
                    continue;
                case 3: 
                    if (legeliste.stoerrelse() > 0){
                        brukResept();
                    }
                    else {
                        System.out.println("Det må finnes en lege, en pasient "
                                           + "og et legemiddel i systemet "
                                           + "før du kan legge til resept.\n");
                    }
                    continue;
                case 4: 
                    skrivUtStatistikk();
                    continue;
            }
        }
    }


    public void skrivUtHovedmeny()
    {
        System.out.println("Velkommen til Legesystemet.\n");
        System.out.println("Du har følgende valg:");
        System.out.println("1: Skriver ut informasjon.");
        System.out.println("2: Legg til nye instanser.");
        System.out.println("3: Bruk resept.");
        System.out.println("4: Skriv ut statistikk.");
        System.out.println("0: Avslutt Legesystemet.");
    }


    public void skrivUtLeggTilMeny()
    {
        System.out.println("Velkommen til menyen for å legge til elementer.\n");
        System.out.println("Du har følgende valg:");
        System.out.println("1: Legg til pasient.");
        System.out.println("2: Legg til lege.");
        System.out.println("3: Legg til legemiddel.");
        System.out.println("4: Legg til resept.");
        System.out.println("0: Gå tilbake til hovedmeny.");
    }


    public void leggTilMeny()
    {
        int valg = 0;


        while(true) {
            skrivUtLeggTilMeny();
            valg = hentValg();

            switch(valg)
            {
                case 0: 
                    return;
                case 1: 
                    leggTilPasientMeny();
                    continue;
                case 2: 
                    leggTilLegeMeny();
                    continue;
                case 3: 
                    leggTilLegemiddelMeny();
                    continue;
                case 4: 
                    leggTilReseptMeny();
                    continue;
            }
        }
    }


    public int hentValg(){
        int valg = 0;

        while(true){
            try {
                valg = input.nextInt();
                input.nextLine();

                return valg;

            } catch(InputMismatchException e) {
                System.out.println("Kun kommandoer tall er gyldige input.");
                input.next();
                continue;
            }
        }
    }


    public void skrivUtOversikt()
    {
        System.out.println("Alle leger i systemet:");
        for (Lege lege: this.legeliste) {
            lege.skrivUtLegeinformasjon();
        } 

        System.out.println("Alle resepter i systemet:");
        for (Resept resept: this.reseptliste) {
            resept.skrivUtReseptinformasjon();
        }

        System.out.println("Alle legemidler i systemet:");
        for (Legemiddel legemiddel: this.legemiddelliste) {
            legemiddel.skrivUtLegemiddelinformasjon();
        }

        System.out.println("Alle pasienter i systemet:");
        for (Pasient pasient: this.pasientliste) {
            pasient.skrivUtPasientinformasjon();
        }
    }


    public void brukResept()
    {
        int pasient = -1;
        int resept = -1;
        int i = 0;
        int j = 0;

        while(pasient == -1) {
            System.out.println("Hvilken pasient vil du se resepter for?");

            for (Pasient p : pasientliste) {
                System.out.printf("%d: %s (fnr %s)%n", 
                                  i, p.hentNavn(), p.hentFnr());
              i++;
            }
            try {
                pasient = input.nextInt();
                input.nextLine();
            } catch(InputMismatchException e) {
                System.out.println("Kun tall er gyldige input.");
                input.next();
            } catch(UgyldigListeIndeks l) {
                System.out.println("Kun tall fra listen er gyldige.");
                input.next();
            }
        }


        System.out.printf("Pasient: %s (fnr %s)%n", 
                          pasientliste.hent(pasient).hentNavn(), 
                          pasientliste.hent(pasient).hentFnr());

        while(resept == -1) {
            System.out.println("Hvilken resept vil du bruke?");
            for (Resept r : pasientliste.hent(pasient).hentReseptliste()){
                System.out.printf("%d: %s (%d reit)%n", j, 
                                r.hentLegemiddel().hentNavn(), r.hentReit());
                j++;
            }
            try {
                resept = input.nextInt();
                input.nextLine();

            } catch(InputMismatchException e) {
                System.out.println("Kun tall er gyldige input.");
                input.next();

            } catch(UgyldigListeIndeks l) {
                System.out.println("Kun tall fra listen er gyldige.");
                input.next();
            }

        }


        if (pasientliste.hent(pasient).hentReseptliste().hent(resept).bruk()){
            System.out.printf("Brukte resept på %s. Antall gjenværende reit: %d%n",
                              pasientliste.hent(pasient).hentReseptliste().hent(resept).hentLegemiddel().hentNavn(),
                              pasientliste.hent(pasient).hentReseptliste().hent(resept).hentReit());
        } else {
            System.out.printf("Kunne ikke bruke resept på %s (ingen gjenværende reit).%n", pasientliste.hent(pasient).hentReseptliste().hent(resept).hentLegemiddel().hentNavn());
        }
    }


    public void skrivUtStatistikk()
    {
        int antallVanedannende = 0;
        for (Resept r : reseptliste) {
            if((r.hentLegemiddel() instanceof LegemiddelA) 
                    || (r.hentLegemiddel() instanceof LegemiddelB)){
                antallVanedannende++;
      }
    }

    System.out.printf("Utskrevne resepter på vanedannende stoffer: %d%n", 
                      antallVanedannende);

    int antallMilitaerVanedannende = 0;
    for (Resept r : reseptliste) {
        if (r instanceof Militaerresept) {
            if((r.hentLegemiddel() instanceof LegemiddelA) 
               || (r.hentLegemiddel() instanceof LegemiddelB)){
                antallMilitaerVanedannende++;
            }
        }
    }

    System.out.printf("Antall resepter på vanedannende stoffer skrevet ut til militære: %d%n", antallMilitaerVanedannende);

    System.out.println("Leger som har skrevet ut resepter på narkotiske stoffer:");
    for (Lege l : legeliste) {
        for (Resept r : l.hentReseptliste()) {
            if (r.hentLegemiddel() instanceof LegemiddelA) {
                System.out.printf("-%s%n", l.hentNavn());
                break;
            }
        }
    }

    int antallNarkotiske = 0;
    boolean skrivUt = false;

    System.out.println("Pasienter som har resept på narkotiske stoffer:");
    for (Pasient p : pasientliste) {
        for(Resept r : p.hentReseptliste()){
            if(r.hentLegemiddel() instanceof LegemiddelA){
                skrivUt = true;
                antallNarkotiske++;
        }
      }
      if (skrivUt) {
            System.out.printf("-%s, %d resepter%n", p.hentNavn(), antallNarkotiske);
            skrivUt = false;
            antallNarkotiske = 0;
        }
    }
}


    public String hentString()
    {
        String tekststreng;
        while(true) {
            try {
                tekststreng = input.nextLine();
                return tekststreng;
            } catch(InputMismatchException e) {
                System.out.println("Feil: skriv inn tekststreng.");
                input.next();
                continue;
            }
        }
    }


    public void leggTilPasientMeny()
    {
        int valg = 0;


        while(true) {

            System.out.println("Velkommen til menyen for å legge til en pasient.\n");
            System.out.println("Skriv inn pasientens navn og fødselsnummer.");
            System.out.println("Navn:");
            String tmpNavn = hentString();
            System.out.println("Fødselsnummer:");
            String tmpFnr = hentString();

            pasientliste.leggTil(new Pasient(tmpNavn, tmpFnr));
            System.out.println("Vil du legge til enda en pasient (1) eller gå tilbake(0)?");
            valg = hentValg();

            switch(valg)
            {
                case 0: 
                    return;
                case 1: 
                    continue;
            }
        }
    }

    
    public void leggTilLegeMeny()
    {
        int valg = 0;
        boolean erFastlege = false;
        String tmpNavn;
        int tmpAvtalenummer = -1;

        System.out.println("Vil du legge til en fastlege(1) eller en vanlig lege(2)?");
        while(valg == 0) {
            valg = hentValg();
            switch(valg)
            {
                case 1: 
                    erFastlege = true;
                    break;
                case 2: 
                    break;
                default: 
                    System.out.println("Feil: Velg fastlege(1) eller vanlig lege(2):");
                    valg = 0;
            }
        }
        
        System.out.printf("Skriv inn legens navn:");
        tmpNavn = hentString();
        if (erFastlege) {
            while(tmpAvtalenummer == -1) {
                System.out.printf("Skriv inn legens avtalenummer:");
                try {
                    tmpAvtalenummer = input.nextInt();
                    input.nextLine();

                } catch(InputMismatchException e) {
                    System.out.println("Kun tall er gyldige input.");
                    input.next();
                }
            }

            legeliste.leggTil(new Fastlege(tmpNavn, tmpAvtalenummer));
            System.out.println("Fastlege lagt til i Legesystem.\n");
        }
        else {
            legeliste.leggTil(new Lege(tmpNavn));
            System.out.println("Lege lagt til i Legesystem.\n");
        }

    }


    public void leggTilLegemiddelMeny()
    {
        int valg = 0;

        String tmpNavn;
        int tmpStyrke = -1;
        double tmpPris = -1;
        double tmpVirkestoff = -1;
        System.out.println("Meny for å legge til legemiddel.\n");

        System.out.println("Navn: ");
        tmpNavn = hentString();

        while(tmpVirkestoff == -1) {
            System.out.println("Mengde virkestoff: ");
            try {
                tmpVirkestoff = input.nextFloat();
                input.nextLine();

            } catch(InputMismatchException e) {
                System.out.println("Kun tall er gyldige input.");
                input.next();
            }
        }

        while(tmpPris == -1) {
            System.out.println("Pris: ");
            try {
                tmpPris = input.nextFloat();
                input.nextLine();

            } catch(InputMismatchException e) {
                System.out.println("Kun tall er gyldige input.");
                input.next();
            }
        }

        System.out.println("Hva slags legemiddel vil du legge til?");
        System.out.println("1: A");
        System.out.println("2: B");
        System.out.println("3: C");

        while(valg == 0) {
            valg = hentValg();
            switch(valg)
            {
                case 1: 
                    while(tmpStyrke == -1) {
                        System.out.println("Styrke: ");
                        try {
                            tmpStyrke = input.nextInt();
                            input.nextLine();

                        } catch(InputMismatchException e) {
                            System.out.println("Kun tall er gyldige input.");
                            input.next();
                        }
                    }
                    legemiddelliste.leggTil(new LegemiddelA(tmpNavn, tmpPris, 
                                            tmpVirkestoff, tmpStyrke));
                    break;
                case 2: 
                    while(tmpStyrke == -1) {
                        System.out.println("Styrke: ");
                        try {
                            tmpStyrke = input.nextInt();
                            input.nextLine();

                        } catch(InputMismatchException e) {
                            System.out.println("Kun tall er gyldige input.");
                            input.next();
                        }
                    }
                    legemiddelliste.leggTil(new LegemiddelB(tmpNavn, tmpPris, 
                                            tmpVirkestoff, tmpStyrke));
                    break;
                case 3:
                    legemiddelliste.leggTil(new LegemiddelC(tmpNavn, tmpPris, 
                                            tmpVirkestoff));
                    break;
                default: 
                    System.out.println("Feil: Velg 1, 2 eller 3.:");
                    valg = 0;
            }
        }
    }


    public Legemiddel hentLegemiddelnavn()
    {
        String in;
        while(true) {
            in = hentString();
            for (Legemiddel legemiddel: legemiddelliste) {
                if (legemiddel.hentNavn().equals(in)) {
                    return legemiddel;
                }
            }
            System.out.println("Velg et gyldig legemiddel!\n");
        }
    }



    public Pasient hentPasientId()
    {
        int in = -1;
        while(true) {
            while(in == -1) {
                System.out.printf("Skriv inn pasientId:");
                try {
                    in = input.nextInt();
                    input.nextLine();

                } catch(InputMismatchException e) {
                    System.out.println("Kun tall er gyldige input.");
                    input.next();
                }
            }
            for (Pasient pasient: pasientliste) {
                if (pasient.hentId() == in) {
                    return pasient;
                }
            }
            in = -1;
            System.out.println("Velg en gyldig pasient!\n");
        }
    }


    public Lege hentLegenavn()
    {
        String in;
        while(true) {
            in = hentString();
            for (Lege lege: legeliste) {
                if (lege.hentNavn().equals(in)) {
                    return lege;
                }
            }
            System.out.println("Velg en gyldig lege!\n");
        }
    }


    public void leggTilReseptMeny()
    {
        int valg = 0;
        int reit = 0;

        Legemiddel tmpLegemiddel;
        Lege tmpLege;
        Pasient tmpPasient;

        System.out.println("Meny for å legge til resept.\n");

        System.out.println("Legens navn: ");
        tmpLege = hentLegenavn();

        System.out.println("Legemidlets navn: ");
        tmpLegemiddel = hentLegemiddelnavn();

        System.out.println("Pasientens id: ");
        tmpPasient = hentPasientId();

        System.out.println("Hva slags resept vil du legge til?");
        System.out.println("1: Hvit");
        System.out.println("2: Blå");
        System.out.println("3: Prevensjon");
        System.out.println("4: Militær");
        while(valg == 0) {
            valg = hentValg();
            switch(valg)
            {
                case 1: 
                    reit = -1;
                    while(reit == -1) {
                        System.out.println("Reit:");
                        try {
                            reit = input.nextInt();
                            input.nextLine();

                        } catch(InputMismatchException e) {
                            System.out.println("Kun tall er gyldige input.");
                            input.next();
                        }
                    }
                    reseptliste.leggTil(new HvitResept(tmpLegemiddel, 
                                        tmpLege, tmpPasient, reit));
                    break;
                case 2: 
                    reit = -1;
                    while(reit == -1) {
                        System.out.println("Reit:");
                        try {
                            reit = input.nextInt();
                            input.nextLine();

                        } catch(InputMismatchException e) {
                            System.out.println("Kun tall er gyldige input.");
                            input.next();
                        }
                    }
                    reseptliste.leggTil(new BlaaResept(tmpLegemiddel, 
                                        tmpLege, tmpPasient, reit));
                    break;
                case 3: 
                    reseptliste.leggTil(new PResept(tmpLegemiddel, 
                                        tmpLege, tmpPasient, reit));
                    break;
                case 4: 
                    reit = -1;
                    while(reit == -1) {
                        System.out.println("Reit:");
                        try {
                            reit = input.nextInt();
                            input.nextLine();

                        } catch(InputMismatchException e) {
                            System.out.println("Kun tall er gyldige input.");
                            input.next();
                        }
                    }
                    reseptliste.leggTil(new Militaerresept(tmpLegemiddel, 
                                        tmpLege, tmpPasient, reit));
                    break;
                default: 
                    System.out.println("Feil: Velg 1, 2, 3 eller 4.:");
                    valg = 0;
            }
        }
    }
}
