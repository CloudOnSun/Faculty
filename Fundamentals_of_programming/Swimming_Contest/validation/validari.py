from exceptions.erori import ValidError


class Validari(object):
    
    
    def valideaza_participant(self, nrid):
        """
        fucntie care valideaza daca nrid-ul unui participant este >=0
        :param nrid: numarul de identificare al unui participant
        :return: -, daca participantul este valid
        :raises: ValidError cu textul:
                "Numar de identificare invalid\n" daca nrid-ul este <0
        """
        err = ""
        if nrid < 0:
            err += "Numar de identificare invalid\n"
        if len(err) > 0:
            raise ValidError(err)
        
        
    def valideaza_scor(self, scor):
        """
        functie care verifica daca scorul introdus este valid >=1 si <=10
        :param scor: scorul
        :return: -, daca scorul este valid
        :raises: ValidError cu textul:
                "Scor invalid\n" daca scorul nu se afla in intervalul [1,10]
        """
        if scor < 1 and scor != 0 or scor > 10:
            err = "Scor invalid\n"
            raise ValidError(err)
    

    def valideaza_proba(self, proba):
        """
        functie care verifica daca o proba este valida >=1 si <=10
        :param proba: o proba
        :return: -, daca proba este valida
        :raises: ValidError cu textul:
                "Proba invalida!!!"
        """
        if proba < 1 or proba > 10:
            raise ValidError("Proba invalida!!!\n")

    
    def valideaza_interval_participanti(self, lista_participanti, nrcurent1, nrcurent2):
        """
        o functie care verifica daca un interval de participanti este valid
        :param nrcurent1: primul participant din interval
        :param nrcurent2: ultimul participant din interval
        :param lista_participanti: lista de participanti
        :return: -, daca intervalul este valid
        :raises: Exception cu textul:
                "Interval invalid\n"
        """
        if nrcurent1 < 1 or nrcurent1 > lista_participanti.get_lungime_lista_participanti() or nrcurent2 > lista_participanti.get_lungime_lista_participanti() or nrcurent2 < 1 or nrcurent1 > nrcurent2:
            raise ValidError("Interval invalid\n")

    
    def valideaza_scor2(self, scor):
        """
        functie care verifica daca scorul introdus este valid >=1 si <=100
        :param scor: scorul
        :return: -, daca scorul este valid
        :raises: ValidError cu textul:
                "Scor invalid\n" daca scorul nu se afla in intervalul [1,100]
        """
        if scor < 1 or scor > 100:
            raise ValidError("Scor invalid\n")
        
    