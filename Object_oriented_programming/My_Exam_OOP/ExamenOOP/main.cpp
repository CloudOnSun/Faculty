#include "mainActivity.h"
#include <QtWidgets/QApplication>
#include "teste.h"

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
    testAll();
    MainActivity* m = new MainActivity;
    m->show();
    return a.exec();
}
