#include "uml_usecase.h"

UMLUseCase::UMLUseCase()
	: _name( "" )
	, _rect( QRectF( -50, -30, 100, 60 ) )
{
	const QRectF rect( this->boundingRect() );
	const qreal a_offset = rect.height() / 2;
	const qreal b_offset = rect.width() / 2;

	_topPort.setOffset( QPointF( 0, -a_offset ) );
	_bottomPort.setOffset( QPointF( 0, a_offset ) );
	_leftPort.setOffset( QPointF( -b_offset, 0 ) );
	_rightPort.setOffset( QPointF( b_offset, 0 ) );
}

QString UMLUseCase::getName()
{
	return _name;
}

void UMLUseCase::setName( QString name )
{
	_name = name;
}

QRectF UMLUseCase::boundingRect() const
{
    return _rect;
}

void UMLUseCase::paint( QPainter * painter, const QStyleOptionGraphicsItem * option, QWidget * widget )
{
	QPainterPath path;
	path.addEllipse( this->boundingRect() );
	painter->setPen( this->pen() );
	painter->setBrush( QColor( 0, 0, 255, 127 ) );
	painter->drawEllipse( this->boundingRect() );
	QFont font( painter->font() );
	font.setPointSize( 10 );
	painter->setFont( font );
	painter->drawText( this->boundingRect(), Qt::AlignCenter, this->getName() );
	this->UMLObject::paint( painter, option, widget );
}
