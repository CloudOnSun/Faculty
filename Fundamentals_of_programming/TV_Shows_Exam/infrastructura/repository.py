'''
Created on 6 Dec 2021

@author: Admin
'''
from domain.entitati import TvShow
from erori.exceptii import RepoError


class RepoTV(object):
    
    
    def __init__(self, file_path):
        self.__file_path = file_path
        self.__lista = []
        
    
    def __read_from_file(self):
        """
        o functie care citeste toate showurile din fisier
        """
        with open(self.__file_path, "r") as f:
            self.__lista = []
            lines = f.readlines()
            for line in lines:
                line = line.strip()
                if len(line) > 0:
                    parts = line.split(",")
                    id = parts[0]
                    titlu = parts[1]
                    gen = parts[2]
                    nr_ep = int(parts[3])
                    tvshow = TvShow(id, titlu, gen, nr_ep)
                    self.__lista.append(tvshow)
    
    
    def get_all(self):
        """
        o functie care returneaza toate showurile
        :return self.__lista(array) - lista de showuri
        """
        self.__read_from_file()
        return self.__lista

    
    def cauta_dupa_id(self, id):
        """
        o functie care cauta un show dupa un id
        :param id - id-ul unui show
        :return show - un obiect de tip TvShow
        :raises RepoError cu textul "show inexistent\n" daca nu exista niciun show cu id-ul introdus"
        """
        self.__read_from_file()
        for show in self.__lista:
            if show.get_id() == id:
                return show
        raise RepoError("show inexistent\n")
    
    
    
    



