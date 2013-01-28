#include "uml_class.h"

UMLClass::UMLClass()
	: _name( "" )
	, _rect( QRectF( -50, -50, 100, 100 ) )
{
	const QRectF rect( this->boundingRect() );
	const qreal offset = rect.width() / 2;

	_topPort.setOffset( QPointF( 0, -offset ) );
	_bottomPort.setOffset( QPointF( 0, offset ) );
	_leftPort.setOffset( QPointF( -offset, 0 ) );
	_rightPort.setOffset( QPointF( offset, 0 ) );
}

QString UMLClass::getName()
{
	return _name;
}

void UMLClass::setName( QString name )
{
	_name = name;
}

QRectF UMLClass::boundingRect() const
{
    return _rect;
}

void UMLClass::paint( QPainter * painter, const QStyleOptionGraphicsItem * option, QWidget * widget )
{
	const QRectF rect( this->boundingRect() );

	painter->setBrush( QColor( 255, 0, 0, 127 ) );
	painter->setPen( this->pen() );
	painter->drawRect( this->boundingRect() );
	painter->drawLine( QPointF( rect.left(), rect.top() + rect.height() / 3 ), QPointF( rect.right(), rect.top() + rect.height() / 3 ) );
	painter->drawLine( QPointF( rect.left(), rect.top() + rect.height() / 3 * 2 ), QPointF( rect.right(), rect.top() + rect.height() / 3 * 2 ) );
	QFont font( painter->font() );
	font.setPointSize( 10 );
	painter->setFont( font );

	const QRectF fontRect( rect.left(), rect.top(), rect.width(), rect.height() / 3 );
	painter->drawText( fontRect, Qt::AlignCenter, this->getName() );
	this->UMLObject::paint( painter, option, widget );
}
