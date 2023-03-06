#include <QtWidgets/QApplication>
#include "teste.h"
#include "interfata.h"
#include "mainActivityWidget.h"

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);

	
    MainActivityWidget* mainActivityWidget = new MainActivityWidget();

    mainActivityWidget->show();

    return a.exec();
}
