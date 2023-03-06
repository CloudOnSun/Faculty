#pragma once
#include "repository.h"

class SrvMelodii
{
private:

	RepoMelodii& repo;

public:

	SrvMelodii(RepoMelodii& r) : repo{ r } { }


	/*
	* O funcite care returneaza toate melodiile existente in memorie sortate dupa artist
	* return: vector<Melodie> - vectorul cu toate melodiile sortate dupa artist
	*/
	vector<Melodie> getAll();


	/*
	* O functie care creaza o melodie si o adauga in memorie
	* param: titlu : string - titlul melodiei
	* param: artist : string - artistul melodiei
	* param: gen : string - genul melodiei
	* post: melodia a fost adaugata cu un id unic in memorie
	*/
	void adauga(string titlu, string artist, string gen);


	/*
	* O functie care sterge o melodie din memorie dupa un id valid
	* pre: id-ul sa fie valid si sa existe o melodie cu acel id
	* param: id : int - id-ul melodiei
	* post: melodia a fost stearsa
	*/
	void sterge(int id);
};