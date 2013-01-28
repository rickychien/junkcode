#ifndef CHATTERCLIENT_H
#define CHATTERCLIENT_H

#include <QtGui/QMainWindow>
#include <QtNetwork/QTcpSocket>
#include "ui_ChatterClient.h"

class ChatterClient : public QMainWindow 
{
	Q_OBJECT

public:
	ChatterClient( QWidget * parent = 0, Qt::WFlags flags = 0 );
	~ChatterClient();

private slots:
	 void on_loginButton_clicked();
	 void on_sayButton_clicked();
	 void onReadyRead();
	 void onConnected();
	 void onError( QAbstractSocket::SocketError socketError );

private:
	Ui::ChatterClientClass ui;
	QTcpSocket * socket;

};

#endif // CHATTERCLIENT_H
