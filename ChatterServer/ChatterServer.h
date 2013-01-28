#ifndef CHATTERSERVER_H
#define CHATTERSERVER_H

#include <QStringList>
#include <QTcpServer>
#include <QTcpSocket>
#include <QMap>
#include <QSet>

class ChatterServer : public QTcpServer 
{
	Q_OBJECT

public:
	ChatterServer( QObject * parent = 0 );
	~ChatterServer();

private slots:
    void onReadyRead();
    void onDisconnected();
    void sendUserList();
	void onError( QAbstractSocket::SocketError socketError );

protected:
	virtual void incomingConnection( int socketDescriptor );

private:
	QSet< QTcpSocket * > clients;
	QMap< QTcpSocket *, QString > users;

};

#endif // CHATTERSERVER_H
