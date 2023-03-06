'''
Created on 6 Dec 2021

@author: Admin
'''
from domain.entitati import TvShow, ShowEvaluation
from infrastructura.repository import RepoTV
from validare.validatori import ValidTV
from business.servicii import ServiceTV
from erori.exceptii import RepoError

class Teste(object):
    
    
    def __test_creeaza_tvshow(self):
        id = 123
        titlu = "chernobyl"
        gen = "documentar"
        nr_ep = 8
        tvshow = TvShow(id, titlu, gen, nr_ep)
        assert tvshow.get_id() == id
        assert tvshow.get_titlu() == titlu
        assert tvshow.get_gen() == gen
        assert tvshow.get_nr_ep() == nr_ep
    
    
    def __test_get_all_repo(self):
        repo = RepoTV("testing/tvshow_test.txt")
        lista = repo.get_all()
        assert len(lista) == 3
        assert lista[0].get_id() == "COM1"
        assert lista[0].get_titlu() == "The Good Place"
        assert lista[0].get_gen() == "fantasy comedy"
        assert lista[0].get_nr_ep() == 53
        assert lista[1].get_id() == "THR1"
        assert lista[1].get_titlu() == "Mindhunter"
        assert lista[1].get_gen() == "psychological thriller"
        assert lista[1].get_nr_ep() == 19
        assert lista[2].get_id() == "FAN4"
        assert lista[2].get_titlu() == "The Witcher"
        assert lista[2].get_gen() == "fantasy drama"
        assert lista[2].get_nr_ep() == 8
    
    
    def __test_srv_cautare_pe_gen(self):
        repo = RepoTV("testing/tvshow_test.txt")
        valid = ValidTV()
        srv = ServiceTV(valid, repo)
        sir = "fant"
        lista = srv.cauta_dupa_gen(sir)
        assert len(lista) == 2
        assert lista[0].get_id() == "COM1"
        assert lista[0].get_titlu() == "The Good Place"
        assert lista[0].get_gen() == "fantasy comedy"
        assert lista[0].get_nr_ep() == 53
        assert lista[1].get_id() == "FAN4"
        assert lista[1].get_titlu() == "The Witcher"
        assert lista[1].get_gen() == "fantasy drama"
        assert lista[1].get_nr_ep() == 8
    
    
    def __test_cauta_dupa_id_repo(self):
        repo = RepoTV("testing/tvshow_test.txt")
        id = "THR1"
        show = repo.cauta_dupa_id(id)
        assert show.get_id() == "THR1"
        assert show.get_titlu() == "Mindhunter"
        assert show.get_gen() == "psychological thriller"
        assert show.get_nr_ep() == 19
        id2 = 123
        try:
            repo.cauta_dupa_id(id2)
            assert False
        except RepoError as re:
            assert str(re) == "show inexistent\n"
        
    
    
    def __test_get_preference(self):
        id = 123
        titlu = "chernobyl"
        gen = "documentar"
        nr_ep = 9
        tvshow = TvShow(id, titlu, gen, nr_ep)
        nr_ep_vaz = 7
        showEv = ShowEvaluation(tvshow, nr_ep_vaz)
        pref = showEv.get_preference()
        assert pref == "favorit"
        nr_ep_vaz = 4
        showEv = ShowEvaluation(tvshow, nr_ep_vaz)
        pref = showEv.get_preference()
        assert pref == "if_bored"
        nr_ep_vaz = 0
        showEv = ShowEvaluation(tvshow, nr_ep_vaz)
        pref = showEv.get_preference()
        assert pref == "disliked"
    
    
    def __test_srv_info_pref(self):
        repo = RepoTV("testing/tvshow_test.txt")
        valid = ValidTV()
        srv = ServiceTV(valid, repo)
        id = "THR1"
        nr_ep_vaz = 16
        showdto = srv.get_info_pref(id, nr_ep_vaz)
        assert str(showdto) == "Mindhunter psychological thriller favorit"
        id2 = 123
        try:
            srv.get_info_pref(id2, nr_ep_vaz)
            assert False
        except RepoError as re:
            assert str(re) == "show inexistent\n"
    
    
    def run_all_teste(self):
        self.__test_creeaza_tvshow()
        self.__test_get_all_repo()
        self.__test_srv_cautare_pe_gen()
        self.__test_cauta_dupa_id_repo()
        self.__test_get_preference()
        self.__test_srv_info_pref()
    
    



