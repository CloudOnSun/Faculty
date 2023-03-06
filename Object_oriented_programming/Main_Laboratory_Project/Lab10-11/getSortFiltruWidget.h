#pragma once
#include <qwidget.h>
#include "informationFilmWidget.h"
#include <qcombobox.h>
#include "service.h"


class GetSortFiltruWidget : public QWidget
{
	Q_OBJECT

signals:

	void butonApasat(const vector<Film>& listaFilme);

private:

	Service& service;

	void butonGetAllApasat();
	void butonSortareApasat();
	void butonFiltrareApasat();

	void creeazaConexiuni();

public:

	QPushButton* getAllButon;
	QPushButton* sorteazaButon;
	QPushButton* filtreazaButon;
	QComboBox* criteriuSortare;
	QComboBox* criteriuFiltrare;

	GetSortFiltruWidget(Service& service, QWidget* parent = nullptr);
};