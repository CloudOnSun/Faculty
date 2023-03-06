from exceptions.erori import RepoError
from domain.entitati import GParticipant, Participant


class Lista_participanti(object):

    
    def __init__(self, lista_participanti):
        self.__lista_participanti = lista_participanti
    
    
    def adauga_participant_in_lista(self, participant):
        """
        o functie care adauga un participant identificabil unic dupa nrid in lista de participanti
        :param participant: un participant identificabil unic
        :return: -, l'=l U {participant} daca nu mai exista alt participant cu acelasi nrid
        :raises: RepoError cu textul:
                "Participant existent!\n" daca exista deja un participant cu acelasi nrid
        """
        for _participant in self.__lista_participanti:
            if _participant.get_nrid() == participant.get_nrid():
                raise RepoError("Participant existent!\n")
        self.__lista_participanti.append(participant)
        
    
    def get_lungime_lista_participanti(self):
        return len(self.__lista_participanti)
    
    
    def get_participant_din_lista_nrcurent(self, nrcurent):
        """
        o functie care returneaza un participant din lista dupa numarul curent
        """
        participant = self.__lista_participanti[nrcurent]
        return  participant
    
    
    def get_toti_participanti(self):
        """
        o functie care returneaza toti participantii existenti
        """
        lista = []
        for participant in self.__lista_participanti:
            lista.append(participant)
        return lista
    
    
    def get_participant_nrid(self, nrid):
        """
        o functie care returneaza participantul din lista care are un nrid dat
        :param nrid - numarul de identificare al participantului
        :return participant - daca exista
        :raises: RepoError cu textul "Participant inexistent!!\n"
        """
        gasit = False
        for participant in self.__lista_participanti:
            if participant.get_nrid() == nrid:
                gasit = True
                return participant
        if gasit == False:
            raise RepoError("Participant inexistent!!\n")

    
    def copiere(self):
        """
        o functie care face o copie a listei de participanti
        """
        lista = []
        for participant in self.__lista_participanti:
            nrid = participant.get_nrid()
            proba1 = participant.get_proba_1()
            proba2 = participant.get_proba_2()
            proba3 = participant.get_proba_3()
            proba4 = participant.get_proba_4()
            proba5 = participant.get_proba_5()
            proba6 = participant.get_proba_6()
            proba7 = participant.get_proba_7()
            proba8 = participant.get_proba_8()
            proba9 = participant.get_proba_9()
            proba10 = participant.get_proba_10()
            participant_nou = Participant(nrid, proba1, proba2, proba3, proba4, proba5, proba6, proba7, proba8, proba9, proba10)
            lista.append(participant_nou)
        return lista
    
    
    def rescriere_lista(self):
        """
        o functie care goleste lista de participanti
        """
        self.__lista_participanti.clear()
    
    
class Lista_scor_general(object):
    
    
    def __init__(self, lista_scor_general):
        self.__lista_scor_general = lista_scor_general

    
    def calcul_scor_general(self, lista_participanti):
        """
        o functie care calculeaza scorurile generale pentru toti participantii existenti
        """
        lista = Lista_participanti.get_toti_participanti(lista_participanti)
        for participant in lista:
            nrid = participant.get_nrid()
            scor_general = participant.get_scor_general() 
            gparticipant = GParticipant(nrid, scor_general)
            self.__lista_scor_general.append(gparticipant)
            

    def get_scor_general_participant(self, nrid):
        """
        o functie care returneaza scorul general al unui participant dat
        :param nrid- numarul de identificare al participantului
        :return scorul general al participantului, daca exista
        :raises: RepoError cu textul "Participant inexistent!!\n"
        """
        gasit = False
        for gparticipant in self.__lista_scor_general:
            if gparticipant.get_nrid() == nrid:
                gasit = True
                return gparticipant.get_scor_general()
        if gasit == False:
            raise RepoError("Participant inexistent!!\n")

    
    def get_toti_participanti(self):
        """
        o functie care returneaza toti participantii existenti
        """
        lista = []
        for gparticipant in self.__lista_scor_general:
            lista.append(gparticipant)
        return lista
    
    
    def get_lungime_lista_scor_general(self):
        return len(self.__lista_scor_general)


    def get_participant_din_lista_nrcurent(self, nrcurent):
        """
        o functie care returneaza un participant din lista dupa numarul curent
        """
        gparticipant = self.__lista_scor_general[nrcurent]
        return  gparticipant

    
    def ordonare(self):
        """
        o functie care ordoneaza lista scorurilor generale descrescator dupa scor
        """
        for i in range(len(self.__lista_scor_general)-1):
            for j in range(i+1, len(self.__lista_scor_general)):
                if self.__lista_scor_general[i].get_scor_general() < self.__lista_scor_general[j].get_scor_general():
                    aux = self.__lista_scor_general[i]
                    self.__lista_scor_general[i] = self.__lista_scor_general[j]
                    self.__lista_scor_general[j] = aux
                
    
    def calcul_medie(self):
        """
        o functie care calculeaza media scorurilor
        """
        suma = 0
        for gparticipant in self.__lista_scor_general:
            suma += gparticipant.get_scor_general()
        if len(self.__lista_scor_general) != 0:
            medie = suma / len(self.__lista_scor_general)
            return medie
        else: 
            raise RepoError("Introduceti mai intai participanti!")
    
    
    def calcul_scor_minim(self):
        """
        o functie care calculeaza scorul minim 
        """
        minim = 101
        for gparticipant in self.__lista_scor_general:
            if gparticipant.get_scor_general() < minim:
                minim = gparticipant.get_scor_general()
        return minim
        
                
class Undo_list(object):
    
    
    def __init__(self, lista_actiuni):
        self.__lista_actiuni = lista_actiuni
    
    
    def put_in_undo(self, lista_participanti):
        """
        o functie care pune noua lista de participanti in lista de actiuni
        """
        lista_copie = lista_participanti.copiere()
        self.__lista_actiuni.append(lista_copie)
        
        
    def pop_from_undo(self):
        """ 
        o functie care returneaza ultima actiune efectuata pe lista de participanti 
        si o sterge din lista de actiuni
        :return lista_copie - ultima lista de participanti
        :raises: RepoError daca nu se mai poate face undo cu textul:
                "Nu se mai poate face undo!\n"
        """
        if len(self.__lista_actiuni) > 0:
            lista_copie = self.__lista_actiuni[-1]
            self.__lista_actiuni.pop(-1)
            return lista_copie
        else:
            raise RepoError("Nu se mai poate face undo!\n")
    

    def get_last_from_undo(self):
        """ 
        o functie care doar returneaza ultima actiune efectuata pe lista de participanti 
        :return lista_copie - ultima lista de participanti
        :raises: RepoError daca nu se mai poate face undo cu textul:
                "Nu se mai poate face undo!\n"
        """
        if len(self.__lista_actiuni) > 0:
            return self.__lista_actiuni[-1]
        else:
            raise RepoError("Nu se mai poate face undo!\n")

    
    def get_lungime_undo(self):
        return len(self.__lista_actiuni)
    
    