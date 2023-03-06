#pragma once
#include "service.h"

class InterfataUtilizator
{
private:
	Service& service;

public:
	InterfataUtilizator(Service& service) noexcept :service{ service } {}

	InterfataUtilizator(const InterfataUtilizator& altaInterfata) = delete;

	void afiseaza_meniu();

	void afiseaza_meniu_cos_cumparturi();

	void adauga_film();

	void sterge_film();

	void cauta_film();

	void modifica_film();

	void afiseaza_toate_filmele();

	void filtreaza_filmele();

	void sorteaza_filmele();

	void adauga_film_cos_cumparaturi();

	void goleste_cos_cumparaturi();

	void afiseaza_cos_cumparaturi();

	void adauga_filme_random_cos_cumparaturi();

	void export_in_fisier();

	void undo();

	void runMeniuCos();

	void runUI();
};