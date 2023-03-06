#include "informationFilmWidget.h"
#include <qformlayout.h>

TextFilmWidget::TextFilmWidget(QString& informatieDorita, QWidget* parent) : QWidget(parent), informatieDorita{ informatieDorita }
{
	this->linieText = new QLineEdit;
	this->butonOK = new QPushButton("OK");
	QFormLayout* formLayout = new QFormLayout;
	formLayout->addRow(informatieDorita, linieText);
	formLayout->addWidget(butonOK);
	this->setLayout(formLayout);
	this->conecteazaActiune();
}


/*void TextFilmWidget::verificaTitluFilm()
{
	try
	{
		string titlu = this->linieTitlu->text().toStdString();
		service.cautaFilm(titlu);
		emit titluValid();
	}
	catch (ExceptieRepository& exceptie)
	{
		QMessageBox::warning(this, "Error", QString::fromStdString(exceptie.get_mesaj()));
		return;
	}
}*/

void TextFilmWidget::executaActiune()
{
	rezultat = new QString(linieText->text());
	emit textIntrodus(rezultat);
	this->close();
}


void TextFilmWidget::conecteazaActiune()
{
	QWidget::connect(this->butonOK, &QPushButton::clicked, [&]() { this->executaActiune(); });

}


void NumarFilmWidget::executaActiune()
{
	rezultat = new int(this->boxNumar->value());
	emit numarIntrodus(rezultat);
	this->close();
}


void NumarFilmWidget::conecteazaActiune()
{
	QWidget::connect(this->butonOk, &QPushButton::clicked, [&]() {this->executaActiune(); });
}