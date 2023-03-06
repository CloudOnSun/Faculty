from infrastructure.repo_participant import Lista_participanti, Undo_list
from presentation.afisari import Afisari
from presentation.intrari import Intrari
from business.service_participant import Service
from exceptions.erori import ValidError, RepoError


class Consola(object):
    
    
    def __ui_funct1_ex1(self, lista_participanti):
        """
        o functie care executa functionalitatea 1, subpunctul 1:
                Adauga scor pentru un nou participant
        """
        try:
            srv = Service()
            citire = Intrari()
            nrid = citire.ui_cititi_participant()
            print("Cititi scorul pentru proba 1: ")
            proba1 = citire.ui_cititi_scor()
            print("Cititi scorul pentru proba 2: ")
            proba2 = citire.ui_cititi_scor()
            print("Cititi scorul pentru proba 3: ")
            proba3 = citire.ui_cititi_scor()
            print("Cititi scorul pentru proba 4: ")
            proba4 = citire.ui_cititi_scor()
            print("Cititi scorul pentru proba 5: ")
            proba5 = citire.ui_cititi_scor()
            print("Cititi scorul pentru proba 6: ")
            proba6 = citire.ui_cititi_scor()
            print("Cititi scorul pentru proba 7: ")
            proba7 = citire.ui_cititi_scor()
            print("Cititi scorul pentru proba 8: ")
            proba8 = citire.ui_cititi_scor()
            print("Cititi scorul pentru proba 9: ")
            proba9 = citire.ui_cititi_scor()
            print("Cititi scorul pentru proba 10: ")
            proba10 = citire.ui_cititi_scor()
            srv.adauga_participant_in_lista(lista_participanti, nrid, proba1, proba2, proba3, proba4, proba5, proba6, proba7, proba8, proba9, proba10)
            afisare =Afisari()
            afisare.ui_print_participanti(lista_participanti)
        except ValueError as er:
            print(er)
        except ValidError as ve:
            print(ve)
        except RepoError as re:
            print(re)
        
    
    def __ui_funct1_ex2(self, lista_participanti):
        """
        o functie care executa functionalitatea 1, subpunctul 1:
                Inserare scor pentru un participant
        """
        try:
            citire = Intrari()
            nrid = citire.ui_cititi_participant()
            proba = citire.ui_cititi_proba()
            scor = citire.ui_cititi_scor()
            srv = Service()
            srv.adauga_scor_participant(lista_participanti, nrid, proba, scor)
            afisare = Afisari()
            afisare.ui_print_participanti(lista_participanti)
        except ValueError as er:
            print(er)
        except ValidError as ve:
            print(ve)
        except RepoError as re:
            print(re)
    
    
    def __ui_funct1(self, lista_participanti, undo_list):
        """
        o functie pentru functionalitatea 1
        return:
        """
        while True:
            afisare = Afisari()
            afisare.ui_print_submeniu1()
            comanda = Intrari()
            cmd1 = comanda.ui_get_command()
            srv = Service()
            if cmd1 == "3":
                return
            elif cmd1 == "1":
                srv.update_undo_list(undo_list, lista_participanti)
                self.__ui_funct1_ex1(lista_participanti)
            elif cmd1 == "2":
                srv.update_undo_list(undo_list, lista_participanti)
                self.__ui_funct1_ex2(lista_participanti)
            else:
                print("Comanda invalida!!!\n")
        
    
    def __ui_funct2_ex1(self, lista_participanti):
        """
        o functie pentru functionalitatea 2, subpunctul 1:
                Sterge scorul pentru un participant dat la o anumita proba
        """
        try:
            citire = Intrari()
            nrid = citire.ui_cititi_participant()
            proba = citire.ui_cititi_proba()
            srv = Service()
            srv.sterge_scor_participant(lista_participanti, nrid, proba)
            afisare = Afisari()
            afisare.ui_print_participanti(lista_participanti)
        except ValueError as er:
            print(er)
        except ValidError as ve:
            print(ve)
        except RepoError as re:
            print(re)
        
    
    def __ui_funct2_ex2(self, lista_participanti):
        """
        o functie pentru functionalitatea 2, subpunctul 1:
                Sterge scorul la o anumita proba pentru un interval de participanti dat 
        """
        try:
            citire = Intrari()
            interval = citire.ui_cititi_interval_participanti()
            proba = citire.ui_cititi_proba()
            srv = Service()
            srv.sterge_scor_interval_participanti(lista_participanti, interval[0], interval[1], proba)
            afisare = Afisari()
            afisare.ui_print_participanti(lista_participanti)
        except ValueError as er:
            print(er)
        except ValidError as ve:
            print(ve)
        except RepoError as re:
            print(re)
    
    
    def __ui_funct2(self, lista_participanti, undo_list):
        """
        o functie pentru functionalitatea 2
        """
        while True:
            afisare = Afisari()
            afisare.ui_print_submeniu2()
            comanda = Intrari()
            cmd1 = comanda.ui_get_command()
            srv = Service()
            if cmd1 == "4":
                return
            elif cmd1 == "1":
                srv.update_undo_list(undo_list, lista_participanti)
                self.__ui_funct2_ex1(lista_participanti)
            elif cmd1 == "2":
                srv.update_undo_list(undo_list, lista_participanti)
                self.__ui_funct2_ex2(lista_participanti)
            elif cmd1 == "3":
                srv.update_undo_list(undo_list, lista_participanti)
                self.__ui_funct1_ex2(lista_participanti)
            else:
                print("Comanda invalida!!!\n")
    
    
    def __ui_funct3_ex1(self, lista_participanti):
        """
        o functie pentru functionalitatea 3, subpunctul 1:
                Tipareste participantii care au scorul general mai mic decat un scor dat.
        """
        try:
            citire = Intrari()
            scor = citire.ui_cititi_scor2()
            srv = Service()
            lista = srv.filtrare_scor_mai_mic(lista_participanti, scor)
            afisare = Afisari()
            afisare.ui_print_participanti_scor_general(lista)
        except ValidError as ve:
            print(ve)
        except ValueError as vr:
            print(vr)
    
    
    def __ui_funct3_ex2(self, lista_participanti):
        """
        o functie pentru functionalitatea 3, subpunctul 2:
                Tipareste participantii ordonati dupa scorul general.
        """
        srv = Service()
        lista = srv.ordonare_dupa_scor(lista_participanti)
        afisare = Afisari()
        afisare.ui_print_participanti_scor_general(lista)
    
    
    def __ui_funct3_ex3(self, lista_participanti):
        """
        o functie pentru functionalitatea 3, subpunctul 3:
                Tipareste participantii ordonati dupa scor si care au scorul general mai mare decat un scor dat.
        """
        try:
            citire = Intrari()
            scor = citire.ui_cititi_scor2()
            srv = Service()
            lista = srv.filtrare_scor_mai_mare(lista_participanti, scor)
            afisare = Afisari()
            afisare.ui_print_participanti_scor_general(lista)
        except ValidError as ve:
            print(ve)
        except ValueError as vr:
            print(vr)
    
    
    def __ui_funct3(self, lista_participanti):
        """
        o functie pentru functionalitatea 3
        """
        while True:
            afisare = Afisari()
            afisare.ui_print_submeniu3()
            comanda = Intrari()
            cmd1 = comanda.ui_get_command()
            if cmd1 == "4":
                return
            elif cmd1 == "1":
                self.__ui_funct3_ex1(lista_participanti)
            elif cmd1 == "2":
                self.__ui_funct3_ex2(lista_participanti)
            elif cmd1 == "3":
                self.__ui_funct3_ex3(lista_participanti)
            else:
                print("Comanda invalida!!!\n")
    
    
    def __ui_funct4_ex1(self, lista_participanti):
        """
        o functie pentru functionalitatea 4, subpunctul 1:
                Calculeaza media scorurilor pentru un interval dat
        """
        try:
            citire = Intrari()
            interval = citire.ui_cititi_interval_participanti()
            srv = Service()
            media = srv.calcul_media_scorurilor(lista_participanti, interval[0], interval[1])
            print(media)
        except ValidError as ve:
            print(ve)
        except ValueError as vr:
            print(vr)
        except RepoError as re:
            print(re)
    
    
    def __ui_funct4_ex2(self, lista_participanti):
        """
        o functie pentru functionalitatea 4, subpunctul 2:
                Calculeaza scorul minim pentru un interval dat
        """
        try:
            citire = Intrari()
            interval = citire.ui_cititi_interval_participanti()
            srv = Service()
            minim = srv.calcul_scor_minim_pe_interval(lista_participanti, interval[0], interval[1])
            print(minim)
        except ValidError as ve:
            print(ve)
        except ValueError as vr:
            print(vr)
        except RepoError as re:
            print(re)
    
    
    def __ui_funct4_ex3(self, lista_participanti):
        """
        o functie pentru functionalitatea 4, subpunctul 3:
                Tipareste participantii dintr-un interval dat care au scorul multiplu de 10.
        """
        try:
            citire = Intrari()
            interval = citire.ui_cititi_interval_participanti()
            srv = Service()
            lista= srv.filtrare_scor_multiplu_de_un_numar(lista_participanti, 10, interval[0], interval[1])
            afisare = Afisari()
            afisare.ui_print_participanti_scor_general(lista)
        except ValidError as ve:
            print(ve)
        except ValueError as vr:
            print(vr)
        except RepoError as re:
            print(re)
    
    
    def __ui_funct4(self, lista_participanti):
        """
        o functie pentru functionalitatea 4
        """
        while True:
            afisare = Afisari()
            afisare.ui_print_submeniu4()
            comanda = Intrari()
            cmd1 = comanda.ui_get_command()
            if cmd1 == "4":
                return
            elif cmd1 == "1":
                self.__ui_funct4_ex1(lista_participanti)
            elif cmd1 == "2":
                self.__ui_funct4_ex2(lista_participanti)
            elif cmd1 == "3":
                self.__ui_funct4_ex3(lista_participanti)
            else:
                print("Comanda invalida!!!\n")
    
    
    def __ui_funct5_ex1(self, lista_participanti):
        """
        o functie pentru functionalitatea 5, subpunctul 1:
                Tipareste participantii dintr-un interval dat care au scorul multiplu de un numar.
        """
        try:
            citire = Intrari()
            scor = citire.ui_cititi_scor3()
            srv = Service()
            lista= srv.filtrare_scor_multiplu_de_un_numar(lista_participanti, scor, 1, lista_participanti.get_lungime_lista_participanti())
            afisare = Afisari()
            afisare.ui_print_participanti_scor_general(lista)
        except ValidError as ve:
            print(ve)
        except ValueError as vr:
            print(vr)
        except RepoError as re:
            print(re)
    
    
    def __ui_funct5(self, lista_participanti):
        """
        o functie pentru functionalitatea 5
        """
        while True:
            afisare = Afisari()
            afisare.ui_print_submeniu5()
            comanda = Intrari()
            cmd1 = comanda.ui_get_command()
            if cmd1 == "3":
                return
            elif cmd1 == "1":
                self.__ui_funct5_ex1(lista_participanti)
            elif cmd1 == "2":
                self.__ui_funct3_ex1(lista_participanti)
            else:
                print("Comanda invalida!!!\n")

    
    def __ui_undo(self, undo_list, lista_participanti):
        """
        o functie care face undo
        """
        while True:
            afisare = Afisari()
            afisare.ui_print_submeniu6()
            comanda = Intrari()
            cmd1 = comanda.ui_get_command()
            if cmd1 == "1":
                try:  
                    srv = Service()
                    srv.undo(undo_list, lista_participanti)
                    afisare = Afisari()
                    afisare.ui_print_participanti(lista_participanti)
                except RepoError as re:
                    print(re)
            elif cmd1 == "2":
                return
            else:
                print("Comanda invalida!!!\n")
    
    
    def run(self):
        lista = []
        lista_participanti = Lista_participanti(lista)
        lista2 = []
        undo_list = Undo_list(lista2)
        while True:
            afisare = Afisari()
            afisare.ui_print_meniu()
            comanda = Intrari()
            cmd = comanda.ui_get_command()
            if cmd == "7":
                return
            elif cmd == "1":
                self.__ui_funct1(lista_participanti, undo_list)
            elif cmd == "2":
                self.__ui_funct2(lista_participanti, undo_list)
            elif cmd == "3":
                self.__ui_funct3(lista_participanti)
            elif cmd == "4":
                self.__ui_funct4(lista_participanti)
            elif cmd == "5":
                self.__ui_funct5(lista_participanti)
            elif cmd == "6":
                self.__ui_undo(undo_list, lista_participanti)
            else:
                print("Comanda invalida!!!\n")
    
    
class Consola_batch_mode(object):
        
    
    def __ui_funct1_ex1(self, lista_participanti, batch):
        """
        o functie care executa functionalitatea 1, subpunctul 1:
                Adauga scor pentru un nou participant
        """
        print("adaugare -> nou")
        if len(batch) != 11:
            print("Comenzi Insuficiente -> adaugare -> nou")
        else:
            try:
                srv = Service()
                try:
                    print("nrid")
                    nrid = int(batch[0])
                    print("scorul pentru proba 1: ")
                    proba1 = int(batch[1])
                    print("scorul pentru proba 2: ")
                    proba2 = int(batch[2])
                    print("scorul pentru proba 3: ")
                    proba3 = int(batch[3])
                    print("scorul pentru proba 4: ")
                    proba4 = int(batch[4])
                    print("scorul pentru proba 5: ")
                    proba5 = int(batch[5])
                    print("scorul pentru proba 6: ")
                    proba6 = int(batch[6])
                    print("scorul pentru proba 7: ")
                    proba7 = int(batch[7])
                    print("scorul pentru proba 8: ")
                    proba8 = int(batch[8])
                    print("scorul pentru proba 9: ")
                    proba9 = int(batch[9])
                    print("scorul pentru proba 10: ")
                    proba10 = int(batch[10])
                except ValueError:
                    raise ValueError("Valoare invalida!\n")
                srv.adauga_participant_in_lista(lista_participanti, nrid, proba1, proba2, proba3, proba4, proba5, proba6, proba7, proba8, proba9, proba10)
                afisare =Afisari()
                afisare.ui_print_participanti(lista_participanti)
            except ValueError as er:
                print(er)
            except ValidError as ve:
                print(ve)
            except RepoError as re:
                print(re)
        
    
    def __ui_funct1_ex2(self, lista_participanti, batch):
        """
        o functie care executa functionalitatea 1, subpunctul 1:
                Inserare scor pentru un participant
        """
        print("adaugare -> inserare")
        if len(batch) != 3:
            print("Comenzi Insuficiente -> adaugare -> inserare")
        else:
            try:
                try:
                    print("nrid")
                    nrid = int(batch[0])
                    print("proba")
                    proba = int(batch[1])
                    print("scor")
                    scor = int(batch[2])
                except ValueError:
                    raise ValueError("Valoare invalida")
                srv = Service()
                srv.adauga_scor_participant(lista_participanti, nrid, proba, scor)
                afisare = Afisari()
                afisare.ui_print_participanti(lista_participanti)
            except ValueError as er:
                print(er)
            except ValidError as ve:
                print(ve)
            except RepoError as re:
                print(re)
    
    
    def __ui_funct1(self, lista_participanti, undo_list, batch):
        """
        o functie pentru functionalitatea 1
        return:
        """ 
        print("adaugare")
        srv = Service()
        if len(batch) == 0:
            print("Comenzi Insuficiente -> adaugare")
        else:
            if batch[0] == "nou":
                srv.update_undo_list(undo_list, lista_participanti)
                self.__ui_funct1_ex1(lista_participanti, batch[1:])
            elif batch[1] == "inserare":
                srv.update_undo_list(undo_list, lista_participanti)
                self.__ui_funct1_ex2(lista_participanti, batch[1:])
            else:
                print("Comanda invalida!!!\n")
        
    
    def __ui_funct2_ex1(self, lista_participanti, batch):
        """
        o functie pentru functionalitatea 2, subpunctul 1:
                Sterge scorul pentru un participant dat la o anumita proba
        """
        print("modificare -> stergere")
        if len(batch) != 2:
            print("Comenzi Insuficiente -> modificare -> sterge")
        else:
            try:
                try:
                    print("nrid")
                    nrid = int(batch[0])
                    print("proba")
                    proba = int(batch[1])
                except ValueError:
                        raise ValueError("Valoare invalida")
                srv = Service()
                srv.sterge_scor_participant(lista_participanti, nrid, proba)
                afisare = Afisari()
                afisare.ui_print_participanti(lista_participanti)
            except ValueError as er:
                print(er)
            except ValidError as ve:
                print(ve)
            except RepoError as re:
                print(re)
        
    
    def __ui_funct2_ex2(self, lista_participanti, batch):
        """
        o functie pentru functionalitatea 2, subpunctul 1:
                Sterge scorul la o anumita proba pentru un interval de participanti dat 
        """
        print("modificare -> stergere_interval")
        if len(batch) != 3:
            print("Comenzi Insuficiente -> modificare -> sterge_interval")
        else:
            try:
                try:
                    print("primul participant din interval")
                    nrcurent1 = int(batch[0])
                    print("al doilea participant din interval")
                    nrcurent2 = int(batch[1])
                    print("proba")
                    proba = int(batch[2])
                except ValueError:
                    raise ValueError("Valoare invalida")
                srv = Service()
                srv.sterge_scor_interval_participanti(lista_participanti, nrcurent1, nrcurent2, proba)
                afisare = Afisari()
                afisare.ui_print_participanti(lista_participanti)
            except ValueError as er:
                print(er)
            except ValidError as ve:
                print(ve)
            except RepoError as re:
                print(re)
    
    
    def __ui_funct2(self, lista_participanti, undo_list, batch):
        """
        o functie pentru functionalitatea 2
        """
        print("modificare")
        srv = Service()
        if len(batch) == 0:
            print("Comenzi Insuficiente -> modificare")
        else:
            if batch[0] == "iesire":
                return
            elif batch[0] == "sterge":
                srv.update_undo_list(undo_list, lista_participanti)
                self.__ui_funct2_ex1(lista_participanti, batch[1:])
            elif batch[0] == "sterge_interval":
                srv.update_undo_list(undo_list, lista_participanti)
                self.__ui_funct2_ex2(lista_participanti, batch[1:])
            elif batch[0] == "inlocuieste":
                srv.update_undo_list(undo_list, lista_participanti)
                self.__ui_funct1_ex2(lista_participanti, batch[1:])
            else:
                print("Comanda invalida!!!\n")
    
    
    def __ui_funct3_ex1(self, lista_participanti, batch):
        """
        o functie pentru functionalitatea 3, subpunctul 1:
                Tipareste participantii care au scorul general mai mic decat un scor dat.
        """
        print("tipareste -> mic")
        if len(batch) != 1:
            print("Comenzi Insuficiente -> tipareste -> mic")
        else:
            try:
                try:
                    print("scor")
                    scor = int(batch[0])
                except ValueError:
                    raise ValueError("Valoare invalida")
                srv = Service()
                lista = srv.filtrare_scor_mai_mic(lista_participanti, scor)
                afisare = Afisari()
                afisare.ui_print_participanti_scor_general(lista)
            except ValidError as ve:
                print(ve)
            except ValueError as vr:
                print(vr)
    
    
    def __ui_funct3_ex2(self, lista_participanti):
        """
        o functie pentru functionalitatea 3, subpunctul 2:
                Tipareste participantii ordonati dupa scorul general.
        """
        print("tipareste -> ordonare")
        srv = Service()
        lista = srv.ordonare_dupa_scor(lista_participanti)
        afisare = Afisari()
        afisare.ui_print_participanti_scor_general(lista)
    
    
    def __ui_funct3_ex3(self, lista_participanti, batch):
        """
        o functie pentru functionalitatea 3, subpunctul 3:
                Tipareste participantii ordonati dupa scor si care au scorul general mai mare decat un scor dat.
        """
        print("tipareste -> mare")
        if len(batch) != 1:
            print("Comenzi Insuficiente -> tipareste -> mare")
        else:
            try:
                try:
                    print("scor")
                    scor = int(batch[0])
                except ValueError:
                    raise ValueError("Valoare invalida")
                srv = Service()
                lista = srv.filtrare_scor_mai_mare(lista_participanti, scor)
                afisare = Afisari()
                afisare.ui_print_participanti_scor_general(lista)
            except ValidError as ve:
                print(ve)
            except ValueError as vr:
                print(vr)
        
    
    def __ui_funct3(self, lista_participanti, batch):
        """
        o functie pentru functionalitatea 3
        """
        print("tipareste")
        if len(batch) == 0:
            print("Comenzi Insuficiente -> tipareste")
        else:
            if batch[0] == "iesire":
                return
            elif batch[0] == "mic":
                self.__ui_funct3_ex1(lista_participanti, batch[1:])
            elif batch[0] == "ordonare":
                self.__ui_funct3_ex2(lista_participanti)
            elif batch[0] == "mare":
                self.__ui_funct3_ex3(lista_participanti, batch[1:])
            else:
                print("Comanda invalida!!!\n")
    
    
    def __ui_funct4_ex1(self, lista_participanti, batch):
        """
        o functie pentru functionalitatea 4, subpunctul 1:
                Calculeaza media scorurilor pentru un interval dat
        """
        print("operati -> medie")
        if len(batch) != 2:
            print("Comenzi Insuficiente -> operati -> medie")
        else:
            try:
                try:
                    print("primul participant din interval")
                    nrcurent1 = int(batch[0])
                    print("al doilea participant din interval")
                    nrcurent2 = int(batch[1])
                except ValueError:
                    raise ValueError("Valoare invalida")
                srv = Service()
                media = srv.calcul_media_scorurilor(lista_participanti, nrcurent1, nrcurent2)
                print(media)
            except ValidError as ve:
                print(ve)
            except ValueError as vr:
                print(vr)
            except RepoError as re:
                print(re)
    
    
    def __ui_funct4_ex2(self, lista_participanti, batch):
        """
        o functie pentru functionalitatea 4, subpunctul 2:
                Calculeaza scorul minim pentru un interval dat
        """
        print("operati -> minim")
        if len(batch) != 2:
            print("Comenzi Insuficiente -> operati -> medie")
        else:
            try:
                try:
                    print("primul participant din interval")
                    nrcurent1 = int(batch[0])
                    print("al doilea participant din interval")
                    nrcurent2 = int(batch[1])
                except ValueError:
                    raise ValueError("Valoare invalida")
                srv = Service()
                minim = srv.calcul_scor_minim_pe_interval(lista_participanti, nrcurent1, nrcurent2)
                print(minim)
            except ValidError as ve:
                print(ve)
            except ValueError as vr:
                print(vr)
            except RepoError as re:
                print(re)
    
    
    def __ui_funct4_ex3(self, lista_participanti, batch):
        """
        o functie pentru functionalitatea 4, subpunctul 3:
                Tipareste participantii dintr-un interval dat care au scorul multiplu de 10.
        """
        print("operati -> multiplu_10")
        if len(batch) != 2:
            print("Comenzi Insuficiente -> operati -> multiplu_10")
        else:
            try:
                try:
                    print("primul participant din interval")
                    nrcurent1 = int(batch[0])
                    print("al doilea participant din interval")
                    nrcurent2 = int(batch[1])
                except ValueError:
                    raise ValueError("Valoare invalida")
                srv = Service()
                lista= srv.filtrare_scor_multiplu_de_un_numar(lista_participanti, 10, nrcurent1, nrcurent2)
                afisare = Afisari()
                afisare.ui_print_participanti_scor_general(lista)
            except ValidError as ve:
                print(ve)
            except ValueError as vr:
                print(vr)
            except RepoError as re:
                print(re)
    
    
    def __ui_funct4(self, lista_participanti, batch):
        """
        o functie pentru functionalitatea 4
        """
        print("operati")
        if len(batch) == 0:
            print("Comenzi Insuficiente -> operati")
        else:
            if batch[0] == "iesire":
                return
            elif batch[0] == "medie":
                self.__ui_funct4_ex1(lista_participanti, batch[1:])
            elif batch[0] == "minim":
                self.__ui_funct4_ex2(lista_participanti, batch[1:])
            elif batch[0] == "multiplu_10":
                self.__ui_funct4_ex3(lista_participanti, batch[1:])
            else:
                print("Comanda invalida!!!\n")
    
    
    def __ui_funct5_ex1(self, lista_participanti, batch):
        """
        o functie pentru functionalitatea 5, subpunctul 1:
                Tipareste participantii dintr-un interval dat care au scorul multiplu de un numar.
        """
        print("filtrare -> multiplu")
        if len(batch) != 1:
            print("Comenzi Insuficiente -> filtrare -> multiplu")
        else:
            try:
                try:
                    print("scor")
                    scor = int(batch[0])
                except ValueError:
                    raise ValueError("Valoare invalida")
                srv = Service()
                lista= srv.filtrare_scor_multiplu_de_un_numar(lista_participanti, scor, 1, lista_participanti.get_lungime_lista_participanti())
                afisare = Afisari()
                afisare.ui_print_participanti_scor_general(lista)
            except ValidError as ve:
                print(ve)
            except ValueError as vr:
                print(vr)
            except RepoError as re:
                print(re)
    
    
    def __ui_funct5(self, lista_participanti, batch):
        """
        o functie pentru functionalitatea 5
        """
        print("filtrare")
        if len(batch) == 0:
            print("Comenzi Insuficiente -> filtrare")
        else:
            if batch[0] == "iesire":
                return
            elif batch[0] == "multiplu":
                self.__ui_funct5_ex1(lista_participanti, batch[1:])
            elif batch[0] == "mic":
                self.__ui_funct3_ex1(lista_participanti, batch[1:])
            else:
                print("Comanda invalida!!!\n")

    
    def __ui_undo(self, undo_list, lista_participanti):
        """
        o functie care face undo
        """
        try:  
            srv = Service()
            srv.undo(undo_list, lista_participanti)
            afisare = Afisari()
            afisare.ui_print_participanti(lista_participanti)
        except RepoError as re:
            print(re)
    
    
    def run(self):
        lista = []
        lista_participanti = Lista_participanti(lista)
        lista2 = []
        undo_list = Undo_list(lista2)
        while True:
            afisare = Afisari()
            afisare.ui_print_meniu2()
            comanda = Intrari()
            cmd = comanda.ui_get_command()
            batch = cmd.split(",")
            for comanda in batch:
                batch1 = comanda.split(" ")
                if batch1[0] == "iesire":
                    return
                elif batch1[0] == "adaugare":
                    self.__ui_funct1(lista_participanti, undo_list, batch1[1:])
                elif batch1[0] == "modificare":
                    self.__ui_funct2(lista_participanti, undo_list, batch1[1:])
                elif batch1[0] == "tipareste":
                    self.__ui_funct3(lista_participanti, batch1[1:])
                elif batch1[0] == "operati":
                    self.__ui_funct4(lista_participanti, batch1[1:])
                elif batch1[0] == "filtrare":
                    self.__ui_funct5(lista_participanti, batch1[1:])
                elif batch1[0] == "undo":
                    self.__ui_undo(undo_list, lista_participanti)
                else:
                    print("Comanda invalida!!!\n")
        
    
    
    