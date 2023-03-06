from domain.entitati import Participant
from validation.validari import Validari
from exceptions.erori import ValidError, RepoError
from infrastructure.repo_participant import Lista_participanti
from business.service_participant import Service
from infrastructure.repo_participant import Lista_scor_general, Undo_list


class Teste(object):
    
    
    def __test_creeaza_participant(self):
        nrid = 123
        proba1 = 1
        proba2 = 1
        proba3 = 1
        proba4 = 1
        proba5 = 1
        proba6 = 1
        proba7 = 1
        proba8 = 1
        proba9 = 1
        proba10 = 1
        participant = Participant(nrid, proba1, proba2, proba3, proba4, proba5, proba6, proba7, proba8, proba9, proba10)
        assert participant.get_nrid() == nrid
        assert abs(participant.get_proba_1()-proba1) < 0.001
        assert abs(participant.get_proba_4()-proba4) < 0.001
        assert abs(participant.get_proba_6()-proba8) < 0.001
    

    def __test_valideaza_participant(self):
        nrid = 123
        validare = Validari()
        validare.valideaza_participant(nrid)
        nrid_rau = -89
        try:
            validare.valideaza_participant(nrid_rau)
            assert False
        except ValidError as ve:
            assert str(ve) == "Numar de identificare invalid\n"   
        
    
    def __test_adauga_participant_in_lista(self):
        nrid = 123
        proba1 = 1
        proba2 = 1
        proba3 = 1
        proba4 = 1
        proba5 = 1
        proba6 = 1
        proba7 = 1
        proba8 = 1
        proba9 = 1
        proba10 = 1
        participant = Participant(nrid, proba1, proba2, proba3, proba4, proba5, proba6, proba7, proba8, proba9, proba10)
        lista = []
        lista_participanti = Lista_participanti(lista)
        lista_participanti.adauga_participant_in_lista(participant)
        assert lista_participanti.get_lungime_lista_participanti() == 1
        assert lista_participanti.get_participant_din_lista_nrcurent(0) == participant
        nrid = 123
        proba1 = 1
        proba2 = 1
        proba3 = 1
        proba4 = 1
        proba5 = 1
        proba6 = 1
        proba7 = 1
        proba8 = 1
        proba9 = 1
        proba10 = 1
        participant2 = Participant(nrid, proba1, proba2, proba3, proba4, proba5, proba6, proba7, proba8, proba9, proba10)
        try:
            lista_participanti.adauga_participant_in_lista(participant2)
            assert False
        except RepoError as re:
            assert str(re) == "Participant existent!\n"
    
    
    def __test_srv_adauga_participant_in_lista(self):
        lista = []
        lista_participanti = Lista_participanti(lista)
        nrid = 123
        proba1 = 1
        proba2 = 1
        proba3 = 1
        proba4 = 1
        proba5 = 1
        proba6 = 1
        proba7 = 1
        proba8 = 1
        proba9 = 1
        proba10 = 1
        srv = Service()
        srv.adauga_participant_in_lista(lista_participanti, nrid, proba1, proba2, proba3, proba4, proba5, proba6, proba7, proba8, proba9, proba10)
        assert lista_participanti.get_lungime_lista_participanti() == 1
        participant = lista_participanti.get_participant_din_lista_nrcurent(0)
        assert participant.get_nrid() == 123
        proba1 = 1
        proba2 = 1
        proba3 = 1
        proba4 = 1
        proba5 = 1
        proba6 = 1
        proba7 = 1
        proba8 = 1
        proba9 = 1
        proba10 = 1
        try:
            srv.adauga_participant_in_lista(lista_participanti, nrid, proba1, proba2, proba3, proba4, proba5, proba6, proba7, proba8, proba9, proba10)
            assert False
        except RepoError as re:
            assert str(re) == "Participant existent!\n"
        nrid = -123
        proba1 = 1
        proba2 = 1
        proba3 = 1
        proba4 = 1
        proba5 = 1
        proba6 = 1
        proba7 = 1
        proba8 = 1
        proba9 = 1
        proba10 = 1
        try:
            srv.adauga_participant_in_lista(lista_participanti, nrid, proba1, proba2, proba3, proba4, proba5, proba6, proba7, proba8, proba9, proba10)
            assert False
        except ValidError as vr:
            assert str(vr) == "Numar de identificare invalid\n"    
        nrid = 123
        proba1 = 12
        proba2 = 1
        proba3 = 1
        proba4 = 1
        proba5 = 1
        proba6 = 1
        proba7 = 1
        proba8 = 1
        proba9 = 1
        proba10 = 1
        try:
            srv.adauga_participant_in_lista(lista_participanti, nrid, proba1, proba2, proba3, proba4, proba5, proba6, proba7, proba8, proba9, proba10)
            assert False
        except ValidError as vr:
            assert str(vr) == "Scor invalid\n"
    
    
    def __test_srv_adauga_scor_participant(self):
        nrid = 123
        proba1 = 1
        proba2 = 1
        proba3 = 1
        proba4 = 1
        proba5 = 1
        proba6 = 1
        proba7 = 1
        proba8 = 1
        proba9 = 1
        proba10 = 1
        lista = []
        lista_participanti = Lista_participanti(lista)
        srv = Service()
        participant = srv.adauga_participant_in_lista(lista_participanti, nrid, proba1, proba2, proba3, proba4, proba5, proba6, proba7, proba8, proba9, proba10)
        srv.adauga_scor_participant(lista_participanti, 123, 5, 8)
        assert participant.get_proba(5) == 8
        try:
            srv.adauga_scor_participant(lista_participanti, 123, 12, 4)
            assert False
        except ValidError as ve:
            assert str(ve) == "Proba invalida!!!\n"
        try:
            srv.adauga_scor_participant(lista_participanti, 123, 6, 19)
            assert False
        except ValidError as ve:
            assert str(ve) == "Scor invalid\n"
        try:
            srv.adauga_scor_participant(lista_participanti, 124, 6, 3)
            assert False
        except RepoError as ve:
            assert str(ve) == "Participant inexistent!!\n"
    
    
    def __test_srv_sterge_scor_participant(self):
        nrid = 123
        proba1 = 1
        proba2 = 1
        proba3 = 1
        proba4 = 1
        proba5 = 1
        proba6 = 1
        proba7 = 1
        proba8 = 1
        proba9 = 1
        proba10 = 1
        lista = []
        lista_participanti = Lista_participanti(lista)
        srv = Service()
        participant = srv.adauga_participant_in_lista(lista_participanti, nrid, proba1, proba2, proba3, proba4, proba5, proba6, proba7, proba8, proba9, proba10)
        srv.sterge_scor_participant(lista_participanti, nrid, 2)
        assert participant.get_proba(2) == 0
        try:
            srv.sterge_scor_participant(lista_participanti, 124, 3)
            assert False
        except RepoError as ve:
            assert str(ve) == "Participant inexistent!!\n" 
        try:
            srv.sterge_scor_participant(lista_participanti, 123, -3)
            assert False  
        except ValidError as ve:
            assert str(ve) == "Proba invalida!!!\n" 
    
    
    def __test_valideaza_proba(self):
        validare = Validari()
        validare.valideaza_proba(2)
        try:
            validare.valideaza_proba(-1)
            assert False
        except ValidError as ve:
            assert str(ve) == "Proba invalida!!!\n"
    
    
    def __test_valideaza_scor(self):
        validare = Validari()
        validare.valideaza_scor(2)
        try:
            validare.valideaza_scor(-1)
            assert False
        except ValidError as ve:
            assert str(ve) == "Scor invalid\n"
    
    
    def __test_valideaza_interval_participanti(self):
        validare = Validari()
        nrid = 123
        proba1 = 1
        proba2 = 1
        proba3 = 1
        proba4 = 1
        proba5 = 1
        proba6 = 1
        proba7 = 1
        proba8 = 1
        proba9 = 1
        proba10 = 1
        lista = []
        lista_participanti = Lista_participanti(lista)
        srv = Service()
        srv.adauga_participant_in_lista(lista_participanti, nrid, proba1, proba2, proba3, proba4, proba5, proba6, proba7, proba8, proba9, proba10)
        nrid = 124
        proba1 = 1
        proba2 = 1
        proba3 = 1
        proba4 = 1
        proba5 = 1
        proba6 = 1
        proba7 = 1
        proba8 = 1
        proba9 = 1
        proba10 = 1
        srv.adauga_participant_in_lista(lista_participanti, nrid, proba1, proba2, proba3, proba4, proba5, proba6, proba7, proba8, proba9, proba10)
        nrcurent1 = 1
        nrcurent2 = 2
        validare.valideaza_interval_participanti(lista_participanti, nrcurent1, nrcurent2)
        try:
            validare.valideaza_interval_participanti(lista_participanti, 2, 1)
            assert False
        except ValidError as ve:
            assert str(ve) == "Interval invalid\n"
        try:
            validare.valideaza_interval_participanti(lista_participanti, -3, 1)
            assert False
        except ValidError as ve:
            assert str(ve) == "Interval invalid\n"
        try:
            validare.valideaza_interval_participanti(lista_participanti, 1, 9)
            assert False
        except ValidError as ve:
            assert str(ve) == "Interval invalid\n"
    
    
    def __test_srv_sterge_scor_interval_participanti(self):
        nrid = 123
        proba1 = 1
        proba2 = 1
        proba3 = 1
        proba4 = 1
        proba5 = 1
        proba6 = 1
        proba7 = 1
        proba8 = 1
        proba9 = 1
        proba10 = 1
        lista = []
        lista_participanti = Lista_participanti(lista)
        srv = Service()
        participant1 = srv.adauga_participant_in_lista(lista_participanti, nrid, proba1, proba2, proba3, proba4, proba5, proba6, proba7, proba8, proba9, proba10)
        nrid = 124
        proba1 = 1
        proba2 = 1
        proba3 = 1
        proba4 = 1
        proba5 = 1
        proba6 = 1
        proba7 = 1
        proba8 = 1
        proba9 = 1
        proba10 = 1
        participant2 = srv.adauga_participant_in_lista(lista_participanti, nrid, proba1, proba2, proba3, proba4, proba5, proba6, proba7, proba8, proba9, proba10)
        nrcurent1 = 1
        nrcurent2 = 2
        proba = 2
        srv.sterge_scor_interval_participanti(lista_participanti, nrcurent1, nrcurent2, proba)
        assert participant1.get_proba(2) == 0
        assert participant2.get_proba(2) == 0
        try:
            srv.sterge_scor_interval_participanti(lista_participanti, -2, 2, 3)
            assert False
        except ValidError as ve:
            assert str(ve) == "Interval invalid\n"
        try:
            srv.sterge_scor_interval_participanti(lista_participanti, 2, 1, 3)
            assert False
        except ValidError as ve:
            assert str(ve) == "Interval invalid\n"
        try:
            srv.sterge_scor_interval_participanti(lista_participanti, 1, 3, 3)
            assert False
        except ValidError as ve:
            assert str(ve) == "Interval invalid\n"
        try:
            srv.sterge_scor_interval_participanti(lista_participanti, 1, 2, -1)
            assert False
        except ValidError as ve:
            assert str(ve) == "Proba invalida!!!\n"
    
    
    def __test_calcul_scor_general(self):
        lista = []
        lista_scor_general = Lista_scor_general(lista)
        nrid = 123
        proba1 = 1
        proba2 = 1
        proba3 = 1
        proba4 = 1
        proba5 = 1
        proba6 = 1
        proba7 = 1
        proba8 = 1
        proba9 = 1
        proba10 = 1
        lista2 = []
        lista_participanti = Lista_participanti(lista2)
        srv = Service()
        srv.adauga_participant_in_lista(lista_participanti, nrid, proba1, proba2, proba3, proba4, proba5, proba6, proba7, proba8, proba9, proba10)
        nrid = 124
        proba1 = 2
        proba2 = 1
        proba3 = 1
        proba4 = 1
        proba5 = 1
        proba6 = 3
        proba7 = 1
        proba8 = 1
        proba9 = 1
        proba10 = 1
        srv.adauga_participant_in_lista(lista_participanti, nrid, proba1, proba2, proba3, proba4, proba5, proba6, proba7, proba8, proba9, proba10)
        lista_scor_general.calcul_scor_general(lista_participanti)
        assert lista_scor_general.get_scor_general_participant(123) == 10
        assert lista_scor_general.get_scor_general_participant(124) == 13
        assert lista_scor_general.get_lungime_lista_scor_general() == 2
    
    
    def __test_ordonare_dupa_scor_general(self):
        lista = []
        lista_scor_general = Lista_scor_general(lista)
        nrid = 123
        proba1 = 1
        proba2 = 1
        proba3 = 1
        proba4 = 1
        proba5 = 1
        proba6 = 1
        proba7 = 1
        proba8 = 1
        proba9 = 1
        proba10 = 1
        lista2 = []
        lista_participanti = Lista_participanti(lista2)
        srv = Service()
        srv.adauga_participant_in_lista(lista_participanti, nrid, proba1, proba2, proba3, proba4, proba5, proba6, proba7, proba8, proba9, proba10)
        nrid = 124
        proba1 = 2
        proba2 = 1
        proba3 = 1
        proba4 = 1
        proba5 = 1
        proba6 = 3
        proba7 = 1
        proba8 = 1
        proba9 = 1
        proba10 = 1
        srv.adauga_participant_in_lista(lista_participanti, nrid, proba1, proba2, proba3, proba4, proba5, proba6, proba7, proba8, proba9, proba10)
        nrid = 125
        proba1 = 2
        proba2 = 1
        proba3 = 1
        proba4 = 1
        proba5 = 1
        proba6 = 3
        proba7 = 7
        proba8 = 1
        proba9 = 1
        proba10 = 1
        srv.adauga_participant_in_lista(lista_participanti, nrid, proba1, proba2, proba3, proba4, proba5, proba6, proba7, proba8, proba9, proba10)
        lista_scor_general.calcul_scor_general(lista_participanti)
        lista_scor_general.ordonare()
        gparticipant = lista_scor_general.get_participant_din_lista_nrcurent(0)
        assert gparticipant.get_nrid() == 125
        gparticipant = lista_scor_general.get_participant_din_lista_nrcurent(1)
        assert gparticipant.get_nrid() == 124
        gparticipant = lista_scor_general.get_participant_din_lista_nrcurent(2)
        assert gparticipant.get_nrid() == 123
    
    
    def __test_srv_filtrare_participanti_scor_mai_mic(self):
        nrid = 123
        proba1 = 1
        proba2 = 1
        proba3 = 1
        proba4 = 1
        proba5 = 1
        proba6 = 1
        proba7 = 1
        proba8 = 1
        proba9 = 1
        proba10 = 1
        lista2 = []
        lista_participanti = Lista_participanti(lista2)
        srv = Service()
        srv.adauga_participant_in_lista(lista_participanti, nrid, proba1, proba2, proba3, proba4, proba5, proba6, proba7, proba8, proba9, proba10)
        nrid = 124
        proba1 = 2
        proba2 = 1
        proba3 = 1
        proba4 = 1
        proba5 = 1
        proba6 = 3
        proba7 = 1
        proba8 = 1
        proba9 = 1
        proba10 = 1
        srv.adauga_participant_in_lista(lista_participanti, nrid, proba1, proba2, proba3, proba4, proba5, proba6, proba7, proba8, proba9, proba10)
        nrid = 125
        proba1 = 2
        proba2 = 1
        proba3 = 1
        proba4 = 1
        proba5 = 1
        proba6 = 3
        proba7 = 7
        proba8 = 1
        proba9 = 1
        proba10 = 1
        srv.adauga_participant_in_lista(lista_participanti, nrid, proba1, proba2, proba3, proba4, proba5, proba6, proba7, proba8, proba9, proba10)
        lista_scor_general2 = srv.filtrare_scor_mai_mic(lista_participanti, 17)
        gparticipant = lista_scor_general2.get_participant_din_lista_nrcurent(0)
        assert gparticipant.get_nrid() == 124
        gparticipant = lista_scor_general2.get_participant_din_lista_nrcurent(1)
        assert gparticipant.get_nrid() == 123
        assert lista_scor_general2.get_lungime_lista_scor_general() == 2
    
    
    def __test_valideaza_scor2(self):
        validare = Validari()
        validare.valideaza_scor2(24)
        try:
            validare.valideaza_scor2(-1)
            assert False
        except ValidError as ve:
            assert str(ve) == "Scor invalid\n"
        try:
            validare.valideaza_scor2(120)
            assert False
        except ValidError as ve:
            assert str(ve) == "Scor invalid\n"
    
    
    def __test_srv_filtrare_participanti_scor_mai_mare(self):
        nrid = 123
        proba1 = 1
        proba2 = 1
        proba3 = 1
        proba4 = 1
        proba5 = 1
        proba6 = 1
        proba7 = 1
        proba8 = 1
        proba9 = 1
        proba10 = 1
        lista2 = []
        lista_participanti = Lista_participanti(lista2)
        srv = Service()
        srv.adauga_participant_in_lista(lista_participanti, nrid, proba1, proba2, proba3, proba4, proba5, proba6, proba7, proba8, proba9, proba10)
        nrid = 124
        proba1 = 2
        proba2 = 1
        proba3 = 1
        proba4 = 1
        proba5 = 1
        proba6 = 3
        proba7 = 1
        proba8 = 1
        proba9 = 1
        proba10 = 1
        srv.adauga_participant_in_lista(lista_participanti, nrid, proba1, proba2, proba3, proba4, proba5, proba6, proba7, proba8, proba9, proba10)
        nrid = 125
        proba1 = 2
        proba2 = 1
        proba3 = 1
        proba4 = 1
        proba5 = 1
        proba6 = 3
        proba7 = 7
        proba8 = 1
        proba9 = 1
        proba10 = 1
        srv.adauga_participant_in_lista(lista_participanti, nrid, proba1, proba2, proba3, proba4, proba5, proba6, proba7, proba8, proba9, proba10)
        lista_scor_general2 = srv.filtrare_scor_mai_mare(lista_participanti, 11)
        gparticipant = lista_scor_general2.get_participant_din_lista_nrcurent(0)
        assert gparticipant.get_nrid() == 125
        gparticipant = lista_scor_general2.get_participant_din_lista_nrcurent(1)
        assert gparticipant.get_nrid() == 124
        assert lista_scor_general2.get_lungime_lista_scor_general() == 2
    
    
    def __test_srv_ordonare_dupa_scor(self):
        nrid = 123
        proba1 = 1
        proba2 = 1
        proba3 = 1
        proba4 = 1
        proba5 = 1
        proba6 = 1
        proba7 = 1
        proba8 = 1
        proba9 = 1
        proba10 = 1
        lista2 = []
        lista_participanti = Lista_participanti(lista2)
        srv = Service()
        srv.adauga_participant_in_lista(lista_participanti, nrid, proba1, proba2, proba3, proba4, proba5, proba6, proba7, proba8, proba9, proba10)
        nrid = 124
        proba1 = 2
        proba2 = 1
        proba3 = 1
        proba4 = 1
        proba5 = 1
        proba6 = 3
        proba7 = 1
        proba8 = 1
        proba9 = 1
        proba10 = 1
        srv.adauga_participant_in_lista(lista_participanti, nrid, proba1, proba2, proba3, proba4, proba5, proba6, proba7, proba8, proba9, proba10)
        nrid = 125
        proba1 = 2
        proba2 = 1
        proba3 = 1
        proba4 = 1
        proba5 = 1
        proba6 = 3
        proba7 = 7
        proba8 = 1
        proba9 = 1
        proba10 = 1
        srv.adauga_participant_in_lista(lista_participanti, nrid, proba1, proba2, proba3, proba4, proba5, proba6, proba7, proba8, proba9, proba10)
        lista_scor_general2 = srv.ordonare_dupa_scor(lista_participanti)
        gparticipant = lista_scor_general2.get_participant_din_lista_nrcurent(0)
        assert gparticipant.get_nrid() == 125
        gparticipant = lista_scor_general2.get_participant_din_lista_nrcurent(1)
        assert gparticipant.get_nrid() == 124
        gparticipant = lista_scor_general2.get_participant_din_lista_nrcurent(2)
        assert gparticipant.get_nrid() == 123
        assert lista_scor_general2.get_lungime_lista_scor_general() == 3
    
    
    def __test_srv_filtrare_participanti_scor_multiplu_de_un_numar(self):
        nrid = 123
        proba1 = 1
        proba2 = 1
        proba3 = 1
        proba4 = 1
        proba5 = 1
        proba6 = 1
        proba7 = 1
        proba8 = 1
        proba9 = 1
        proba10 = 1
        lista2 = []
        lista_participanti = Lista_participanti(lista2)
        srv = Service()
        srv.adauga_participant_in_lista(lista_participanti, nrid, proba1, proba2, proba3, proba4, proba5, proba6, proba7, proba8, proba9, proba10)
        nrid = 124
        proba1 = 2
        proba2 = 1
        proba3 = 1
        proba4 = 1
        proba5 = 1
        proba6 = 3
        proba7 = 1
        proba8 = 1
        proba9 = 1
        proba10 = 1
        srv.adauga_participant_in_lista(lista_participanti, nrid, proba1, proba2, proba3, proba4, proba5, proba6, proba7, proba8, proba9, proba10)
        nrid = 125
        proba1 = 2
        proba2 = 1
        proba3 = 1
        proba4 = 1
        proba5 = 1
        proba6 = 3
        proba7 = 7
        proba8 = 1
        proba9 = 1
        proba10 = 1
        srv.adauga_participant_in_lista(lista_participanti, nrid, proba1, proba2, proba3, proba4, proba5, proba6, proba7, proba8, proba9, proba10)
        lista_scor_general2 = srv.filtrare_scor_multiplu_de_un_numar(lista_participanti, 5, 1, 3)
        gparticipant = lista_scor_general2.get_participant_din_lista_nrcurent(0)
        assert gparticipant.get_nrid() == 123
        assert lista_scor_general2.get_lungime_lista_scor_general() == 1
        lista_scor_general2 = srv.filtrare_scor_multiplu_de_un_numar(lista_participanti, 7, 1, 3)
        assert lista_scor_general2.get_lungime_lista_scor_general() == 0
        lista_scor_general2 = srv.filtrare_scor_multiplu_de_un_numar(lista_participanti, 19, 1, 3)
        gparticipant = lista_scor_general2.get_participant_din_lista_nrcurent(0)
        assert gparticipant.get_nrid() == 125
        assert lista_scor_general2.get_lungime_lista_scor_general() == 1
        lista_scor_general2 = srv.filtrare_scor_multiplu_de_un_numar(lista_participanti, 1, 1, 3)
        gparticipant = lista_scor_general2.get_participant_din_lista_nrcurent(0)
        assert gparticipant.get_nrid() == 123
        gparticipant = lista_scor_general2.get_participant_din_lista_nrcurent(1)
        assert gparticipant.get_nrid() == 124
        gparticipant = lista_scor_general2.get_participant_din_lista_nrcurent(2)
        assert gparticipant.get_nrid() == 125
        assert lista_scor_general2.get_lungime_lista_scor_general() == 3
    
    
    def __test_calcul_media_scorurilor(self):
        lista = []
        lista_scor_general = Lista_scor_general(lista)
        nrid = 123
        proba1 = 1
        proba2 = 1
        proba3 = 1
        proba4 = 1
        proba5 = 1
        proba6 = 1
        proba7 = 1
        proba8 = 1
        proba9 = 1
        proba10 = 1
        lista2 = []
        lista_participanti = Lista_participanti(lista2)
        srv = Service()
        srv.adauga_participant_in_lista(lista_participanti, nrid, proba1, proba2, proba3, proba4, proba5, proba6, proba7, proba8, proba9, proba10)
        nrid = 124
        proba1 = 2
        proba2 = 1
        proba3 = 1
        proba4 = 1
        proba5 = 1
        proba6 = 3
        proba7 = 1
        proba8 = 1
        proba9 = 1
        proba10 = 1
        srv.adauga_participant_in_lista(lista_participanti, nrid, proba1, proba2, proba3, proba4, proba5, proba6, proba7, proba8, proba9, proba10)
        nrid = 125
        proba1 = 2
        proba2 = 1
        proba3 = 1
        proba4 = 1
        proba5 = 1
        proba6 = 3
        proba7 = 7
        proba8 = 1
        proba9 = 1
        proba10 = 1
        srv.adauga_participant_in_lista(lista_participanti, nrid, proba1, proba2, proba3, proba4, proba5, proba6, proba7, proba8, proba9, proba10)
        lista_scor_general.calcul_scor_general(lista_participanti)
        medie = lista_scor_general.calcul_medie()
        assert abs(medie - (10+13+19)/3) < 0.001
    
    
    def __test_srv_calcul_media_scorurilor(self):
        nrid = 123
        proba1 = 1
        proba2 = 1
        proba3 = 1
        proba4 = 1
        proba5 = 1
        proba6 = 1
        proba7 = 1
        proba8 = 1
        proba9 = 1
        proba10 = 1
        lista2 = []
        lista_participanti = Lista_participanti(lista2)
        srv = Service()
        srv.adauga_participant_in_lista(lista_participanti, nrid, proba1, proba2, proba3, proba4, proba5, proba6, proba7, proba8, proba9, proba10)
        nrid = 124
        proba1 = 2
        proba2 = 1
        proba3 = 1
        proba4 = 1
        proba5 = 1
        proba6 = 3
        proba7 = 1
        proba8 = 1
        proba9 = 1
        proba10 = 1
        srv.adauga_participant_in_lista(lista_participanti, nrid, proba1, proba2, proba3, proba4, proba5, proba6, proba7, proba8, proba9, proba10)
        nrid = 125
        proba1 = 2
        proba2 = 1
        proba3 = 1
        proba4 = 1
        proba5 = 1
        proba6 = 3
        proba7 = 7
        proba8 = 1
        proba9 = 1
        proba10 = 1
        srv.adauga_participant_in_lista(lista_participanti, nrid, proba1, proba2, proba3, proba4, proba5, proba6, proba7, proba8, proba9, proba10)
        medie = srv.calcul_media_scorurilor(lista_participanti, 1, 3)
        assert abs(medie - (10+13+19)/3) < 0.001
        medie = srv.calcul_media_scorurilor(lista_participanti, 1, 2)
        assert abs(medie - (10+13)/2) < 0.001
        medie = srv.calcul_media_scorurilor(lista_participanti, 2, 3)
        assert abs(medie - (13+19)/2) < 0.001
    
    
    def __test_calcul_scor_minim(self):
        lista = []
        lista_scor_general = Lista_scor_general(lista)
        nrid = 123
        proba1 = 1
        proba2 = 1
        proba3 = 1
        proba4 = 1
        proba5 = 1
        proba6 = 1
        proba7 = 1
        proba8 = 1
        proba9 = 1
        proba10 = 1
        lista2 = []
        lista_participanti = Lista_participanti(lista2)
        srv = Service()
        srv.adauga_participant_in_lista(lista_participanti, nrid, proba1, proba2, proba3, proba4, proba5, proba6, proba7, proba8, proba9, proba10)
        nrid = 124
        proba1 = 2
        proba2 = 1
        proba3 = 1
        proba4 = 1
        proba5 = 1
        proba6 = 3
        proba7 = 1
        proba8 = 1
        proba9 = 1
        proba10 = 1
        srv.adauga_participant_in_lista(lista_participanti, nrid, proba1, proba2, proba3, proba4, proba5, proba6, proba7, proba8, proba9, proba10)
        nrid = 125
        proba1 = 2
        proba2 = 1
        proba3 = 1
        proba4 = 1
        proba5 = 1
        proba6 = 3
        proba7 = 7
        proba8 = 1
        proba9 = 1
        proba10 = 1
        srv.adauga_participant_in_lista(lista_participanti, nrid, proba1, proba2, proba3, proba4, proba5, proba6, proba7, proba8, proba9, proba10)
        lista_scor_general.calcul_scor_general(lista_participanti)
        minim = lista_scor_general.calcul_scor_minim()
        assert minim == 10
    
    
    def __test_srv_calcul_scor_minim_pe_interval(self):
        nrid = 123
        proba1 = 1
        proba2 = 1
        proba3 = 1
        proba4 = 1
        proba5 = 1
        proba6 = 1
        proba7 = 1
        proba8 = 1
        proba9 = 1
        proba10 = 1
        lista2 = []
        lista_participanti = Lista_participanti(lista2)
        srv = Service()
        srv.adauga_participant_in_lista(lista_participanti, nrid, proba1, proba2, proba3, proba4, proba5, proba6, proba7, proba8, proba9, proba10)
        nrid = 124
        proba1 = 2
        proba2 = 1
        proba3 = 1
        proba4 = 1
        proba5 = 1
        proba6 = 3
        proba7 = 1
        proba8 = 1
        proba9 = 1
        proba10 = 1
        srv.adauga_participant_in_lista(lista_participanti, nrid, proba1, proba2, proba3, proba4, proba5, proba6, proba7, proba8, proba9, proba10)
        nrid = 125
        proba1 = 2
        proba2 = 1
        proba3 = 1
        proba4 = 1
        proba5 = 1
        proba6 = 3
        proba7 = 7
        proba8 = 1
        proba9 = 1
        proba10 = 1
        srv.adauga_participant_in_lista(lista_participanti, nrid, proba1, proba2, proba3, proba4, proba5, proba6, proba7, proba8, proba9, proba10)
        minim = srv.calcul_scor_minim_pe_interval(lista_participanti, 1, 3)
        assert minim == 10
        minim = srv.calcul_scor_minim_pe_interval(lista_participanti, 1, 2)
        assert minim == 10
        minim = srv.calcul_scor_minim_pe_interval(lista_participanti, 2, 3)
        assert minim == 13
        minim = srv.calcul_scor_minim_pe_interval(lista_participanti, 3, 3)
        assert minim == 19
    
    
    def __test_undo(self):
        lista = []
        undo_list = Undo_list(lista)
        nrid = 123
        proba1 = 1
        proba2 = 1
        proba3 = 1
        proba4 = 1
        proba5 = 1
        proba6 = 1
        proba7 = 1
        proba8 = 1
        proba9 = 1
        proba10 = 1
        lista2 = []
        lista_participanti = Lista_participanti(lista2)
        srv = Service()
        srv.adauga_participant_in_lista(lista_participanti, nrid, proba1, proba2, proba3, proba4, proba5, proba6, proba7, proba8, proba9, proba10)
        nrid = 124
        proba1 = 2
        proba2 = 1
        proba3 = 1
        proba4 = 1
        proba5 = 1
        proba6 = 3
        proba7 = 1
        proba8 = 1
        proba9 = 1
        proba10 = 1
        srv.update_undo_list(undo_list, lista_participanti)
        srv.adauga_participant_in_lista(lista_participanti, nrid, proba1, proba2, proba3, proba4, proba5, proba6, proba7, proba8, proba9, proba10)
        nrid = 125
        proba1 = 2
        proba2 = 1
        proba3 = 1
        proba4 = 1
        proba5 = 1
        proba6 = 3
        proba7 = 7
        proba8 = 1
        proba9 = 1
        proba10 = 1
        srv.update_undo_list(undo_list, lista_participanti)
        srv.adauga_participant_in_lista(lista_participanti, nrid, proba1, proba2, proba3, proba4, proba5, proba6, proba7, proba8, proba9, proba10)
        assert undo_list.get_lungime_undo() == 2
        srv.undo(undo_list, lista_participanti)
        srv.undo(undo_list, lista_participanti)
        assert undo_list.get_lungime_undo() == 0
        try:
            srv.undo(undo_list, lista_participanti)
            assert False
        except RepoError as re:
            assert str(re) == "Nu se mai poate face undo!\n"
    
    
    def run_all_tests(self):
        print("Inceput teste...")
        self.__test_creeaza_participant()
        self.__test_valideaza_participant()
        self.__test_adauga_participant_in_lista()
        self.__test_srv_adauga_participant_in_lista()
        self.__test_srv_adauga_scor_participant()
        self.__test_srv_sterge_scor_participant()
        self.__test_valideaza_proba()
        self.__test_valideaza_scor()
        self.__test_valideaza_interval_participanti()
        self.__test_srv_sterge_scor_interval_participanti()
        self.__test_calcul_scor_general()
        self.__test_ordonare_dupa_scor_general()
        self.__test_srv_filtrare_participanti_scor_mai_mic()
        self.__test_srv_filtrare_participanti_scor_mai_mare()
        self.__test_srv_ordonare_dupa_scor()
        self.__test_valideaza_scor2()
        self.__test_srv_filtrare_participanti_scor_multiplu_de_un_numar()
        self.__test_calcul_media_scorurilor()
        self.__test_srv_calcul_media_scorurilor()
        self.__test_calcul_scor_minim()
        self.__test_srv_calcul_scor_minim_pe_interval()
        self.__test_undo()
        print("Final teste...")
    
    