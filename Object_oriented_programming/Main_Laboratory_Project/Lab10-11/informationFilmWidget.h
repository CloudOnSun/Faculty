#pragma once
#include <qpushbutton.h>
#include <qwidget.h>
#include <qlineedit.h>
#include <qspinbox.h>
#include "qformlayout.h"


class TextFilmWidget : public QWidget
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


};


class NumarFilmWidget : public QWidget
{
	Q_OBJECT

signals:

	void numarIntrodus(int* numar);

private:

	int valoareMinima = 1900;
	int valoareMaxima = 2022;

	int* rezultat = nullptr;
	QString& informatieDorita;
	QSpinBox* boxNumar;
	QPushButton* butonOk;

	void conecteazaActiune();
	void executaActiune();

public:

	explicit NumarFilmWidget(QString& informatieDorita, QWidget* parent = nullptr) : QWidget(parent), informatieDorita{ informatieDorita }
	{
		this->boxNumar = new QSpinBox;
		boxNumar->setMinimum(valoareMinima);
		boxNumar->setMaximum(valoareMaxima);
		this->butonOk = new QPushButton("OK");
		QFormLayout* formLayout = new QFormLayout;
		formLayout->addRow(informatieDorita, boxNumar);
		formLayout->addRow(butonOk);
		this->setLayout(formLayout);
		this->conecteazaActiune();
	}

	NumarFilmWidget(QString& informatieDorita, int valoareMinima, int valoareMaxima, QWidget* parent = nullptr) : QWidget(parent), informatieDorita{ informatieDorita }, valoareMinima{ valoareMinima }, valoareMaxima{ valoareMaxima }
	{
		this->boxNumar = new QSpinBox;
		boxNumar->setMinimum(valoareMinima);
		boxNumar->setMaximum(valoareMaxima);
		this->butonOk = new QPushButton("OK");
		QFormLayout* formLayout = new QFormLayout;
		formLayout->addRow(informatieDorita, boxNumar);
		formLayout->addRow(butonOk);
		this->setLayout(formLayout);
		this->conecteazaActiune();
	}

	NumarFilmWidget(const NumarFilmWidget& altWIdget) = delete;

	NumarFilmWidget& operator=(const NumarFilmWidget& altWidget) = delete;
};