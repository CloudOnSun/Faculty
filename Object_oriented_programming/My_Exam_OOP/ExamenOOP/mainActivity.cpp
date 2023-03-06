#include "mainActivity.h"
#include <qboxlayout.h>
#include <qformlayout.h>
#include <qpainter.h>

MainActivity::MainActivity(QWidget* parent) : QWidget(parent)
{



	tableView->setModel(tableModel);
	tableView->setMaximumHeight(300);
	


	QFormLayout* fLay = new QFormLayout;
	fLay->setAlignment(Qt::AlignCenter);
	fLay->addRow("Titlu", linieTitlu);
	fLay->addRow("Artist", linieArtist);
	fLay->addRow("Gen", linieGen);
	fLay->addWidget(butonAdauga);
	fLay->addWidget(butonSterge);
	//fLay->setVerticalSpacing(30);

	QWidget* op = new QWidget;
	op->setLayout(fLay);


	QHBoxLayout* hLay = new QHBoxLayout;
	hLay->setAlignment(Qt::AlignCenter);
	hLay->addWidget(tableView);
	hLay->addWidget(op);


	this->setLayout(hLay);
	connect();
	repaint();
}


void MainActivity::connect()
{
	QObject::connect(butonAdauga, &QPushButton::clicked, [this]() {

		string titlu = linieTitlu->text().toStdString();
		string artist = linieArtist->text().toStdString();
		string gen = linieGen->text().toStdString();

		srv.adauga(titlu, artist, gen);
		tableModel->setMelodii(srv.getAll());
		repaint();


		});

	QObject::connect(butonSterge, &QPushButton::clicked, [this]() {

		if (tableView->selectionModel()->selectedIndexes().isEmpty())
		{
			return;
		}

		int row = tableView->selectionModel()->selectedIndexes().at(0).row();
		auto celIndex = tableView->model()->index(row, 0);
		int id = tableView->model()->data(celIndex, Qt::DisplayRole).toInt();

		srv.sterge(id);

		tableModel->setMelodii(srv.getAll());
		repaint();

		});

}


void MainActivity::paintEvent(QPaintEvent* ev)
{
	QPainter p{ this };

	auto c1 = QPoint(50, 50);
	auto c2 = QPoint(width() - 50, 50);
	auto c3 = QPoint(50, height() - 50);
	auto c4 = QPoint(width() - 50, height() - 50);

	p.drawEllipse(c1, 10, 10);
	p.drawEllipse(c2, 10, 10);
	p.drawEllipse(c3, 10, 10);
	p.drawEllipse(c4, 10, 10);

	int pop = 0, rock = 0, folk = 0, disco = 0;
	auto melodii = srv.getAll();

	for (auto m : melodii)
	{
		if (m.getGen() == "pop")
		{
			pop++;
			p.drawEllipse(c1, pop * 10 + 10, pop * 10 + 10);
		}
		if (m.getGen() == "folk")
		{
			folk++;
			p.drawEllipse(c2, folk * 10 + 10, folk * 10 + 10);
		}
		if (m.getGen() == "rock")
		{
			rock++;
			p.drawEllipse(c3, rock * 10 + 10, rock * 10 + 10);
		}
		if (m.getGen() == "disco")
		{
			disco++;
			p.drawEllipse(c4, disco * 10 + 10, disco * 10 + 10);
		}
	}


}