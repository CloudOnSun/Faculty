'''
Created on 6 Dec 2021

@author: Admin
'''
from erori.exceptii import RepoError

class Ui(object):

    
    def __init__(self, srv):
        self.__srv = srv
    
    
    
    
    def __print_meniu(self):
        print("1 Cauta show-uri dupa gen")
        print("2 Afisati info preferinte pentru un show")
        print("3 Iesire\n")
    
    
    def __cauta_dupa_gen(self):
        gen = input("Introduceti genul dorit:")
        try:
            lista = self.__srv.cauta_dupa_gen(gen)
            for show in lista:
                print(str(show))
        except RepoError as re:
            print(str(re)) 
    
    
    def __get_info_pref(self):
        id = input("introduceti id-ul showului: ")
        try:
            nr_ep_vaz = int(input("introduceti numarul de ep vazute: "))
        except ValueError:
            print("Numarul de episoade trebuie sa fie un numar intreg")
            return
        try: 
            show = self.__srv.get_info_pref(id, nr_ep_vaz)
            print(str(show))
        except RepoError as re:
            print(str(re)) 
    
    
    def run(self):
        while True:
            self.__print_meniu()
            cmd = input("\nIntroduceti comanda dorita: ")
            if cmd == "3":
                return 
            elif cmd =="1":
                self.__cauta_dupa_gen()
            elif cmd == "2":
                self.__get_info_pref()
    
    



