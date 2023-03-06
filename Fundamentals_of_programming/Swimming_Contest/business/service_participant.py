from domain.entitati import Participant
from validation.validari import Validari
from infrastructure.repo_participant import Lista_scor_general, Lista_participanti


class Service(object):
    
    
    def adauga_participant_in_lista(self, lista_participanti, nrid, proba1, proba2, proba3, proba4, proba5, proba6, proba7, proba8, proba9, proba10):
        '''
        o functie care adauga in lista de participanti un participant identificabil cu numarul unic de identificare
        nrid si care are la cele 10 probe notele proba1-proba10
        :param lista_participanti: lista de participanti
        :param nrid: numarul unic de identificare al participantului
        :param proba1: nota la proba 1
        :param proba2: nota la proba 2
        :param proba3: nota la proba 3
        :param proba4: nota la proba 4
        :param proba5: nota la proba 5
        :param proba6: nota la proba 6
        :param proba7: nota la proba 7
        :param proba8: nota la proba 8
        :param proba9: nota la proba 9
        :param proba10: nota la proba 10
        '''
        participant = Participant(nrid, proba1, proba2, proba3, proba4, proba5, proba6, proba7, proba8, proba9, proba10)
        validare = Validari()
        validare.valideaza_participant(nrid)
        validare.valideaza_scor(proba1)
        validare.valideaza_scor(proba2)
        validare.valideaza_scor(proba3)
        validare.valideaza_scor(proba4)
        validare.valideaza_scor(proba5)
        validare.valideaza_scor(proba6)
        validare.valideaza_scor(proba7)
        validare.valideaza_scor(proba8)
        validare.valideaza_scor(proba9)
        validare.valideaza_scor(proba10)
        lista_participanti.adauga_participant_in_lista(participant)
        return participant
       
        
    def adauga_scor_participant(self, lista_participanti, nrid, proba, scor):
        """
        o functie care adauga o nota pentru un participant la o anumita proba
        :param lista_participanti - lista de participanti
        :param nrid - numarul unic de identificare al participantului
        :param proba - proba data
        :param scor - scorul dat
        """
        validare = Validari()
        validare.valideaza_participant(nrid)
        validare.valideaza_scor(scor)
        validare.valideaza_proba(proba)
        participant = lista_participanti.get_participant_nrid(nrid)
        participant.set_proba(proba, scor)

    
    def sterge_scor_participant(self, lista_participanti, nrid, proba):
        """
        o functie care sterge scorul unui participant dat la o anumita proba(il face 0)
        :param lista_participanti - lista de participanti
        :param nrid - numarul unic de identificare al participantului
        :param proba - proba data
        """
        validare = Validari()
        validare.valideaza_participant(nrid)
        validare.valideaza_proba(proba)
        participant = lista_participanti.get_participant_nrid(nrid)
        participant.set_proba(proba, 0)

    
    def sterge_scor_interval_participanti(self, lista_participanti, nrcurent1, nrcurent2, proba):
        """
        o functie care sterge scorul la o anumita proba pentru un interval de participanti
        :param lista_participanti - lista de participanti
        :param proba - proba data
        :param nrcurent1: primul participant din interval
        :param nrcurent2: ultimul participant din interval
        """
        validare = Validari()
        validare.valideaza_interval_participanti(lista_participanti, nrcurent1, nrcurent2)
        validare.valideaza_proba(proba)
        for contor in range(nrcurent1-1, nrcurent2):
            participant = lista_participanti.get_participant_din_lista_nrcurent(contor)
            participant.set_proba(proba, 0)

    
    def filtrare_scor_mai_mic(self, lista_participanti, scor):
        """
        o functie care filtreaza participantii care au scorul general mai mic decat un scor dat
        :param lista_participanti - lista de participanti
        :param scor - scorul dat
        :return lista_scor_general2 - o lista cu participantii care au scorul general mai mic decat 'scor'
                                        si scorurile lor generale                     
        """
        validare = Validari()
        validare.valideaza_scor2(scor)
        lista1 = []
        lista_scor_general2 = Lista_scor_general(lista1)
        lista2 = []
        lista_participanti2 = Lista_participanti(lista2)
        lista = lista_participanti.get_toti_participanti()
        for participant in lista:
            if participant.get_scor_general() <= scor:
                lista_participanti2.adauga_participant_in_lista(participant)
        lista_scor_general2.calcul_scor_general(lista_participanti2)
        lista_scor_general2.ordonare()
        return lista_scor_general2
    
    
    def ordonare_dupa_scor(self, lista_participanti):
        """
        o functie care ordoneaza participantii descrescator dupa scorul general
        :param lista_participanti - lista de participanti
        """
        lista = []
        lista_scor_general = Lista_scor_general(lista)
        lista_scor_general.calcul_scor_general(lista_participanti)
        lista_scor_general.ordonare()
        return lista_scor_general

    
    def filtrare_scor_mai_mare(self, lista_participanti, scor):
        """
        o functie care filtreaza participantii care au scorul general mai mic decat un scor dat
        :param lista_participanti - lista de participanti
        :param scor - scorul dat
        """
        validare = Validari()
        validare.valideaza_scor2(scor)
        lista1 = []
        lista_scor_general2 = Lista_scor_general(lista1)
        lista2 = []
        lista_participanti2 = Lista_participanti(lista2)
        lista = lista_participanti.get_toti_participanti()
        for participant in lista:
            if participant.get_scor_general() >= scor:
                lista_participanti2.adauga_participant_in_lista(participant)
        lista_scor_general2.calcul_scor_general(lista_participanti2)
        lista_scor_general2.ordonare()
        return lista_scor_general2
    
    
    def filtrare_scor_multiplu_de_un_numar(self, lista_participanti, scor, nrcurent1, nrcurent2):
        """
        o functie care filtreaza participantii care au scorul general mai mic decat un scor dat
        :param lista_participanti - lista de participanti
        :param scor - scorul dat
        :param nrcurent1: primul participant din interval
        :param nrcurent2: ultimul participant din interval
        """
        validare = Validari()
        validare.valideaza_scor2(scor)
        validare.valideaza_interval_participanti(lista_participanti, nrcurent1, nrcurent2)
        lista3 = []
        lista_participanti3 = Lista_participanti(lista3)
        for contor in range(nrcurent1-1, nrcurent2):
            participant = lista_participanti.get_participant_din_lista_nrcurent(contor)
            lista_participanti3.adauga_participant_in_lista(participant)
        lista1 = []
        lista_scor_general2 = Lista_scor_general(lista1)
        lista2 = []
        lista_participanti2 = Lista_participanti(lista2)
        lista = lista_participanti3.get_toti_participanti()
        for participant in lista:
            if participant.get_scor_general() % scor == 0:
                lista_participanti2.adauga_participant_in_lista(participant)
        lista_scor_general2.calcul_scor_general(lista_participanti2)
        return lista_scor_general2

    
    def calcul_media_scorurilor(self, lista_participanti, nrcurent1, nrcurent2):
        """
        o functie care calculeaza media scorurilor pe un interval dat
        :param lista_participanti - lista de participanti
        :param nrcurent1: primul participant din interval
        :param nrcurent2: ultimul participant din interval
        """
        lista = []
        lista_scor_general = Lista_scor_general(lista)
        validare = Validari()
        validare.valideaza_interval_participanti(lista_participanti, nrcurent1, nrcurent2)
        lista2 = []
        lista_participanti2 = Lista_participanti(lista2)
        for contor in range(nrcurent1-1, nrcurent2):
            participant = lista_participanti.get_participant_din_lista_nrcurent(contor)
            lista_participanti2.adauga_participant_in_lista(participant)
        lista_scor_general.calcul_scor_general(lista_participanti2)
        medie = lista_scor_general.calcul_medie()
        return medie

    
    def calcul_scor_minim_pe_interval(self, lista_participanti, nrcurent1, nrcurent2):
        """
        o functie care calculeaza scorul minim pe un interval dat
        :param lista_participanti - lista de participanti
        :param nrcurent1: primul participant din interval
        :param nrcurent2: ultimul participant din interval
        """
        lista = []
        lista_scor_general = Lista_scor_general(lista)
        validare = Validari()
        validare.valideaza_interval_participanti(lista_participanti, nrcurent1, nrcurent2)
        lista2 = []
        lista_participanti2 = Lista_participanti(lista2)
        for contor in range(nrcurent1-1, nrcurent2):
            participant = lista_participanti.get_participant_din_lista_nrcurent(contor)
            lista_participanti2.adauga_participant_in_lista(participant)
        lista_scor_general.calcul_scor_general(lista_participanti2)
        minim = lista_scor_general.calcul_scor_minim()
        return minim

    
    def update_undo_list(self, undo_list, lista_participanti):
        """
        o functie care face update la lista de undo cu ultima activitate de pe lista de participanti
        :param lista_participanti - lista de participanti
        :param undo_list - lista cu toate activitatile listei de participanti
        """
        undo_list.put_in_undo(lista_participanti)

    
    def undo(self, undo_list, lista_participanti):
        """
        o functie care face undo; pune in lista de participanti ultima activitate retinuta in undo_list
        :param lista_participanti - lista de participanti
        :param undo_list - lista cu toate activitatile listei de participanti
        """
        lista_noua = undo_list.pop_from_undo()
        lista_participanti.rescriere_lista()
        for participant in lista_noua:
            lista_participanti.adauga_participant_in_lista(participant)
        return lista_noua
    
    