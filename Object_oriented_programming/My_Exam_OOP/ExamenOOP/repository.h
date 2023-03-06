#pragma once
#include "domain.h"
#include <fstream>

class RepoMelodii
{
private:

	string file;
	vector<Melodie> melodii;

	/*
	* O functie care citeste melodiile din fisier
	*/
	void citesteDinFisier();

	/*
	* O functie care incarca melodiile in fisier
	*/
	void incaracaInFisier();

public:

	RepoMelodii(string f) : file{ f } { citesteDinFisier(); }


	/*
	* O functie care returneaza un vector cu toate melodiile existente in memorie
	* return: v : vector<Melodii> - un vector cu toate melodiile existente;
	*/
	vector<Melodie> getAll();


	/*
	* O functie care incearca sa adauge o melodie in memorie
	* pre: m sa fie Melodie
	* param: m : Melodie - melodie de adaugat
	* Exceptie: daca mai exista o melodie cu acelasi id arunca o exceptie
	* post: melodia, daca este valida, a fost adaugata
	*/
	void adauga(Melodie m);


	/*
	* O functie care sterge o melodie din memorie dupa un id valid
	* post: id valid;
	* param: id : int - id-ul valid al unei melodii existente
	* pre: melodia cu id-ul id este stearsa
	*/
	void sterge(int id);


};