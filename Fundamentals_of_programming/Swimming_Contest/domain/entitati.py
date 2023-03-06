class Participant(object):
    
    
    def __init__(self, nrid, proba1, proba2, proba3, proba4, proba5, proba6, proba7, proba8, proba9, proba10):
        self.__nrid = nrid
        self.__proba1 = proba1
        self.__proba2 = proba2
        self.__proba3 = proba3
        self.__proba4 = proba4
        self.__proba5 = proba5
        self.__proba6 = proba6
        self.__proba7 = proba7
        self.__proba8 = proba8
        self.__proba9 = proba9
        self.__proba10 = proba10
    
    
    def calcul_scor_general(self):
        scor_general = self.__proba1 + self.__proba2 + self.__proba3 + self.__proba4 + self.__proba5 + self.__proba6 + self.__proba7 + self.__proba8 + self.__proba9 + self.__proba10 
        return scor_general

    
    def get_scor_general(self):
        return self.calcul_scor_general()
    

    def get_nrid(self):
        return self.__nrid
    
    
    def get_proba(self, proba):
        """
        o functie care returneaza nota la o anumita proba
        :param: proba - proba data
        """
        if proba == 1:
            return self.get_proba_1()
        elif proba == 2:
            return self.get_proba_2()
        elif proba == 3:
            return self.get_proba_3()
        elif proba == 4:
            return self.get_proba_4()
        elif proba == 5:
            return self.get_proba_5()
        elif proba == 6:
            return self.get_proba_6()
        elif proba == 7:
            return self.get_proba_7()
        elif proba == 8:
            return self.get_proba_8()
        elif proba == 9:
            return self.get_proba_9()
        elif proba == 10:
            return self.get_proba_10()


    def get_proba_1(self):
        return self.__proba1


    def get_proba_2(self):
        return self.__proba2


    def get_proba_3(self):
        return self.__proba3


    def get_proba_4(self):
        return self.__proba4


    def get_proba_5(self):
        return self.__proba5


    def get_proba_6(self):
        return self.__proba6


    def get_proba_7(self):
        return self.__proba7


    def get_proba_8(self):
        return self.__proba8


    def get_proba_9(self):
        return self.__proba9


    def get_proba_10(self):
        return self.__proba10
    
    
    def set_proba(self, proba, value):
        """
        o functie care seteaza o anumita valoare pentru o proba
        :param: proba = o proba data
        :param: value = nota la acea proba
        """
        if proba == 1:
            self.set_proba_1(value)
        if proba == 2:
            self.set_proba_2(value)
        if proba == 3:
            self.set_proba_3(value)
        if proba == 4:
            self.set_proba_4(value)
        if proba == 5:
            self.set_proba_5(value)
        if proba == 6:
            self.set_proba_6(value)
        if proba == 7:
            self.set_proba_7(value)
        if proba == 8:
            self.set_proba_8(value)
        if proba == 9:
            self.set_proba_9(value)
        if proba == 10:
            self.set_proba_10(value)


    def set_proba_1(self, value):
        self.__proba1 = value


    def set_proba_2(self, value):
        self.__proba2 = value


    def set_proba_3(self, value):
        self.__proba3 = value


    def set_proba_4(self, value):
        self.__proba4 = value


    def set_proba_5(self, value):
        self.__proba5 = value


    def set_proba_6(self, value):
        self.__proba6 = value


    def set_proba_7(self, value):
        self.__proba7 = value


    def set_proba_8(self, value):
        self.__proba8 = value


    def set_proba_9(self, value):
        self.__proba9 = value


    def set_proba_10(self, value):
        self.__proba10 = value
    
        
class GParticipant(object):
    
    
    def __init__(self, nrid, scor_general):
        self.__nrid = nrid
        self.__scor_general = scor_general


    def get_nrid(self):
        return self.__nrid


    def get_scor_general(self):
        return self.__scor_general


    def set_scor_general(self, value):
        self.__scor_general = value

    