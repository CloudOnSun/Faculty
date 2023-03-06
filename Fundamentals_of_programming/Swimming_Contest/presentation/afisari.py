from infrastructure.repo_participant import Lista_participanti, Lista_scor_general


class Afisari(object):
        
        
    def ui_print_meniu(self):
        print("1 Adauga un scor la un participant")
        print("2 Modificare scor")
        print("3 Tipareste lista de participant")
        print("4 Operatii pe un subset de participant")
        print("5 Flitrare")
        print("6 Undo")
        print("7 Iesire aplicatie\n")
    
    
    def ui_print_meniu2(self):
        print("Adauga un scor la un participant (adaugare) ")
        print("    Adauga un scor pentru un participant nou (nou) ")
        print("        nrid, proba1, proba2, proba3, proba4, proba5, proba6, proba7, proba8, proba9, proba10")
        print("    Inserare scor pentru un participant deja existent (inserare) ")
        print("        nrid, proba, scor")
        print("    Iesire submeniu (iesire) \n")
        print("Modificare scor (modificare) ")
        print("    Sterge scorul pentru un participant dat (sterge) ")
        print("        nrid, proba")
        print("    Sterge scorul pentru un interval de participanti (sterge_interval) ")
        print("        primul participant din interval, al doilea participant din interval, proba")
        print("    Inlocuieste scorul pentru un participant dat (inlocuieste) ")
        print("        nrid, proba, scor")
        print("    Iesire submeniu (iesire) \n")
        print("Tipareste lista de participant (tipareste) ")
        print("    Tipareste participantii care au scorul general mai mic decat un scor dat (mic)")
        print("        scor")
        print("    Tipareste participantii ordonat dupa scorul general (ordonare)")
        print("    Tipareste participantii cu scorul general mai mare decat un scor dat, ordonat dupa scor (mare)")
        print("        scor")
        print("    Iesire submeniu (iesire) \n")
        print("Operatii pe un subset de participant (operati) ")
        print("    Calculeaza media scorurilor pentru un interval dat (media)")
        print("        primul participant din interval, al doilea participant din interval")
        print("    Calculeaza scorul minim pentru un interval de participanti dat (minim)")
        print("        primul participant din interval, al doilea participant din interval")
        print("    Tipareste participantii dintr-un interval dat care au scorul multiplu de 10 (multiplu_10)")
        print("        primul participant din interval, al doilea participant din interval")
        print("    Iesire submeniu (iesire) \n")
        print("Flitrare (filtrare) ")
        print("    Filtrare participanti care au scorul multiplu unui numar dat (multiplu)")
        print("        scor")
        print("    Filtrare participanti care au scorul mai mic decat un scor dat (mic)")
        print("        scor")
        print("    Iesire submeniu (iesire) \n")
        print("Undo (undo) ")
        print("Iesire aplicatie (iesire) \n")
    
    
    def ui_print_submeniu1(self):
        print("1 Adauga un scor pentru un participant nou")
        print("2 Inserare scor pentru un participant deja existent")
        print("3 Iesire submeniu\n")
    
    
    def ui_print_submeniu2(self):
        print("1 Sterge scorul pentru un participant dat")
        print("2 Sterge scorul pentru un interval de participanti")
        print("3 Inlocuieste scorul pentru un participant dat")
        print("4 Iesire submeniu\n")
    
    
    def ui_print_submeniu3(self):
        print("1 Tipareste participantii care au scorul general mai mic decat un scor dat")
        print("2 Tipareste participantii ordonat dupa scorul general ")
        print("3 Tipareste participantii cu scorul general mai mare decat un scor dat, ordonat dupa scor")
        print("4 Iesire submeniu\n")
    
    
    def ui_print_submeniu4(self):
        print("1 Calculeaza media scorurilor pentru un interval dat")
        print("2 Calculeaza scorul minim pentru un interval de participanti dat")
        print("3 Tipareste participantii dintr-un interval dat care au scorul multiplu de 10")
        print("4 Iesire submeniu\n")
    
    
    def ui_print_submeniu5(self):
        print("1 Filtrare participanti care au scorul multiplu unui numar dat")
        print("2 Filtrare participanti care au scorul mai mic decat un scor dat.")
        print("3 Iesire submeniu\n")
        
        
    def ui_print_submeniu6(self):
        print("1 Continuati undo")
        print("2 Iesire submeniu\n")    
        
        
    def ui_print_participanti(self, lista_participanti):
        lista2 = []
        lista = Lista_participanti.get_toti_participanti(lista_participanti)
        for participant in lista:
            lista2.clear()
            lista2.append(participant.get_nrid())
            lista2.append(participant.get_proba_1())
            lista2.append(participant.get_proba_2())
            lista2.append(participant.get_proba_3())
            lista2.append(participant.get_proba_4())
            lista2.append(participant.get_proba_5())
            lista2.append(participant.get_proba_6())
            lista2.append(participant.get_proba_7())
            lista2.append(participant.get_proba_8())
            lista2.append(participant.get_proba_9())
            lista2.append(participant.get_proba_10())
            print(lista2)
            
            
    def ui_print_participanti_scor_general(self, lista_scor_general):  
        lista2 = []
        lista = Lista_scor_general.get_toti_participanti(lista_scor_general)  
        for gparticipant in lista:
            lista2.clear()
            lista2.append(gparticipant.get_nrid())
            lista2.append(gparticipant.get_scor_general())   
            print(lista2)

