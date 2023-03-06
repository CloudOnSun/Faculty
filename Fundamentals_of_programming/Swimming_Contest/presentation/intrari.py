class Intrari(object):
  
    
    def ui_get_command(self):
        """
        functie care preia comanda de la utilizator
        :return: comanda
        """
        cmd = input("Introduceti numarul corespunaztor operatiei dorite: \n")
        return cmd
    
    
    def ui_cititi_proba(self):
        """
        o functie care citeste numarul unei probe
        :return: Proba, daca proba este valida
        """
        try:
            nrproba = int(input("Introduceti numarul probei (1-10): \n"))
            return nrproba
        except ValueError:
            raise ValueError("Proba trebuie sa fie un numar intreg!\n")
        
        
    def ui_cititi_scor(self):
        """
        o functie care citeste un scor de la utilizator
        :return: Scorul, daca este valid
        """
        try:
            scor = float(input("Introduceti scorul dorit (1-10): \n"))
            return scor
        except ValueError:
            raise ValueError("Scorul trebuie sa fie numar real intre 1 si 10!\n")
        
        
    def ui_cititi_scor2(self):
        """
        o functie care citeste un scor de la utilizator
        :return: Scorul, daca este valid
        """
        try:
            scor = float(input("Introduceti scorul dorit (1-100): \n"))
            return scor
        except ValueError:
            raise ValueError("Scorul trebuie sa fie numar real intre 1 si 100!\n")
        
        
    def ui_cititi_scor3(self):
        """
        o functie care citeste un scor numar intreg de la utilizator
        :return: Scorul, daca este valid
        """
        try:
            scor = int(input("Introduceti scorul dorit, numar intreg (1-100): \n"))
            return scor
        except ValueError:
            raise ValueError("Scorul trebuie sa fie numar intreg intre 1 si 100!\n")
        
        
    def ui_cititi_participant(self):
        """
        o functie care citeste numarul de identificare al unui participant
        :return: nrid-ul daca este valid
        """
        try:
            nrid = int(input("Alegeti participantul dorit (numarul de indentificare): \n"))
            return nrid
        except ValueError:
            raise ValueError("Numarul de identificare este invaild!\n")
        
    
    def ui_cititi_interval_participanti(self):    
        """
        o functie care citeste un interval de participanti
        :return: [a,b] daca este valid
        """
        try:
            nrid1 = int(input("Alegeti intervalul de participanti dorit (numere curente) (ex: 2 enter 4): \n"))
            nrid2 = int(input())
            return [nrid1, nrid2]
        except ValueError:
            raise Exception("Numarul dat este invaild!\n")

        