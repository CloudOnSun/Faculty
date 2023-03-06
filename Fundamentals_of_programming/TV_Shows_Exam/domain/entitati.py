'''
Created on 6 Dec 2021

@author: Admin
'''
class TvShow(object):
    
    
    def __init__(self, id, titlu, gen, nr_ep):
        self.__id = id
        self.__titlu = titlu
        self.__gen = gen
        self.__nr_ep = nr_ep

    def get_id(self):
        return self.__id


    def get_titlu(self):
        return self.__titlu


    def get_gen(self):
        return self.__gen


    def get_nr_ep(self):
        return self.__nr_ep


    def set_titlu(self, value):
        self.__titlu = value


    def set_gen(self, value):
        self.__gen = value


    def set_nr_ep(self, value):
        self.__nr_ep = value

    def __str__(self):
        return self.__id + " " + self.__titlu + " " + self.__gen + " " + str(self.__nr_ep)
    
    
class ShowEvaluation(object):
    
    
    def __init__(self, tvshow, nr_ep_vaz):
        self.__tvshow = tvshow
        self.__nr_ep_vaz = nr_ep_vaz
        
        
    def get_preference(self):
        """
        o functie care returneaza preferinta utilizatorului
        """
        nr_ep = self.__tvshow.get_nr_ep()
        if self.__nr_ep_vaz > (nr_ep*2/3):
            return "favorit"
        elif self.__nr_ep_vaz > (nr_ep*1/3):
            return "if_bored"
        else:
            return "disliked"
        
class ShowDTO(object):
    
    def __init__(self, titlu, gen, pref):
        self.__titlu = titlu
        self.__gen = gen
        self.__pref = pref
        
        
    def __str__(self):
        return self.__titlu + " " + self.__gen + " " + self.__pref
