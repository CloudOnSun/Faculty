#pragma once
#include "inputFilmeWidget.h"
#include "service.h"
#include <qpushbutton.h>
#include "informationFilmWidget.h"

/*class TextFilmWidget : public QWidget
{
	Q_OBJECT

signals:

	void textIntrodus(QString* text);

private:

	QString* rezultat = nullptr;
	QString& informatieDorita;
	QLineEdit* linieText;
	QPushButton* butonOK;

	void conecteazaActiune();
	void executaActiune();

public:

	TextFilmWidget(QString& informatieDorita, QWidget* parent = nullptr);

};*/


class ModificaWidget : public QWidget
{

	Q_OBJECT

signals:
	void modificareCuSucces();

private:
	
	Service& service;

	QString titluFilm = "";
	QLineEdit* linieGenNou;
	QSpinBox* linieAnAparitieNou;
	QLineEdit* linieActorPrincialNou;
	QPushButton* modificaFilmButon;

	void butonModificaApasat();
	void creeazaConexiuni();

public:

	void setTitluFilm(QString& titlu)
	{
		this->titluFilm = titlu;
	}

	ModificaWidget(Service& service, QWidget* parent = nullptr);

};


class BasicOperationsWidget : public QWidget
{
	Q_OBJECT

signals:
	
	void actiuneCuSucces();

private:

	Service& service;

	void butonAdaugaApasat();
	void butonStergeApasat();
	void butonModificaApasat();
	void butonCautaApasat();
	void butonUndoApasat();

	ModificaWidget* modificaWidget;

	void creeazaConexiuni();


public:

	InputFilmeWidget* inputFilmeWidget;

	QPushButton* adaugaButon;
	QPushButton* stergeButon;
	QPushButton* modificaButon;
	QPushButton* cautaButon;
	QPushButton* undoButon;

	BasicOperationsWidget(Service& service, QWidget* parent = nullptr);

};