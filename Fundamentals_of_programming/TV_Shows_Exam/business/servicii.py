'''
Created on 6 Dec 2021

@author: Admin
'''
from erori.exceptii import RepoError
from domain.entitati import ShowEvaluation, ShowDTO

class ServiceTV(object):
    
    
    def __init__(self, valid, repo):
        self.__valid = valid
        self.__repo = repo

    
    def cauta_dupa_gen(self, sir):
        """
        o functie care returneaza toate show-urile disponibile pentru care genul contine stringul dat de utilizator
        :param sir(string) - un string dat de utilizator
        :return lista(array) - o lista de tvshowuri
        :raises RepoError cu textul "nu exista show-uri cu genul introdus\n" daca nu exista niciun show cu genul introdus"
        """
        lista_raw = self.__repo.get_all()
        lista = []
        for tvshow in lista_raw:
            if sir in tvshow.get_gen():
                lista.append(tvshow)
        if len(lista) == 0:
            raise RepoError("nu exista show-uri cu genul introdus\n")
        return lista

    
    def get_info_pref(self, id, nr_ep_vaz):
        """
        o functie care returneaza informatiile de preferinte pentru un anumit show
        :param id(string) - id-ul showului dorit
        :param nr_ep_vaz(int) - numarul de episoade vazute de utilizator
        :return showDto - un obiect de tipul ShowDto
        :raises RepoError cu textul "show inexistent\n" daca nu exista niciun show cu id-ul introdus"
        """
        show = self.__repo.cauta_dupa_id(id)
        titlu = show.get_titlu()
        gen = show.get_gen()
        showEv = ShowEvaluation(show, nr_ep_vaz)
        pref = showEv.get_preference()
        showDto = ShowDTO(titlu, gen, pref)
        return showDto
    
    
    
    



