'''
Created on 6 Dec 2021

@author: Admin
'''
from infrastructura.repository import RepoTV
from validare.validatori import ValidTV
from business.servicii import ServiceTV
from testing.testare import Teste
from prezentare.user_interface import Ui

if __name__ == '__main__':
    repo = RepoTV("tvshows.txt")
    valid = ValidTV()
    srv = ServiceTV(valid, repo)
    consola = Ui(srv)
    teste = Teste()
    teste.run_all_teste()
    consola.run()