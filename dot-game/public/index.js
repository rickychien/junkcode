/**
 * Create a DotGame
 *
 * @class
 * @author Ricky Chien <ricky060709@gmail.com>
 * @param {string} selector - The selector of a root element for appending the DotGame.
 */
class DotGame {
  constructor(selector) {
    // Binding DOM elements
    this.root = document.querySelector(selector);

    // Binding internal method
    this.getDotSize = this.getDotSize.bind(this);
    this.getDotPosition = this.getDotPosition.bind(this);
    this.getDotHexColor = this.getDotHexColor.bind(this);
    this.onDotTriggered = this.onDotTriggered.bind(this);
    this.drawDot = this.drawDot.bind(this);
    this.drawDotRepeatly = this.drawDotRepeatly.bind(this);

    // Initialization
    this.drawInterval = 1000;
    this.setupDotGame();

    // Start drawing
    this.drawDotRepeatly();
  }

  setupDotGame() {
    let dashboard = document.createElement('div');
    dashboard.classList.add('dashboard');

    this.scoreElm = document.createElement('div');
    this.scoreElm.classList.add('score');
    this.scoreElm.textContent = '0';

    this.dotSpeed = 40;
    this.sliderElm = document.createElement('input');
    this.sliderElm.classList.add('slider');
    this.sliderElm.setAttribute('type', 'range');
    this.sliderElm.setAttribute('min', '10');
    this.sliderElm.setAttribute('max', '100');
    this.sliderElm.setAttribute('value', this.dotSpeed);
    this.sliderElm.addEventListener('change', () => {
      this.dotSpeed = parseInt(this.sliderElm.value);
    });

    dashboard.appendChild(this.scoreElm);
    dashboard.appendChild(this.sliderElm);
    this.root.appendChild(dashboard);

    this.canvasElm = document.createElement('div');
    this.canvasElm.classList.add('canvas');
    this.root.appendChild(this.canvasElm);
  }

  // Get dot size diameter between 10 to 100 randomly
  getDotSize() {
    return Math.floor(Math.random() * 91 + 10);
  }

  // Get dot falling position between 1 to 90 randomly
  getDotPosition() {
    return Math.floor(Math.random() * 90 + 1);
  }

  // Get dot color randomly
  getDotHexColor() {
    return '#' + Math.random().toString(16).substr(-6);
  }

  onDotTriggered({ target }) {
    this.scoreElm.textContent =
      parseInt(this.scoreElm.textContent) + parseInt(target.dataset.score);
    target.remove();
    setTimeout(this.drawDot, this.drawInterval);
  }

  // Generating and drawing a new dot constantly when browser tab is active
  drawDotRepeatly() {
    if (!document.hidden) {
      this.drawDot();
    }
    setTimeout(this.drawDotRepeatly, this.drawInterval);
  }

  // Create and append a new dot element on canvas
  drawDot() {
    let size = this.getDotSize();
    let dotElm = document.createElement('div');

    dotElm.classList.add('dot');
    dotElm.dataset.score = 1 / size * 100;
    dotElm.style.backgroundColor = this.getDotHexColor();
    dotElm.style.width = size + 'px';
    dotElm.style.height = size + 'px';
    dotElm.style.left = this.getDotPosition() + '%';
    dotElm.addEventListener('click', this.onDotTriggered);
    dotElm.addEventListener('touch', this.onDotTriggered);

    let self = this;
    let startTime = null;
    let speed = this.dotSpeed;
    let previousTop = 0;

    function drawFalling(timestamp) {
      startTime = startTime || timestamp;
      if (speed !== self.dotSpeed) {
        startTime = timestamp;
        speed = self.dotSpeed;
        previousTop = dotElm.offsetTop;
      }
      let top = previousTop + speed * (timestamp - startTime) / 1000;
      dotElm.style.top = top + 'px';

      if (top <= self.canvasElm.clientHeight) {
        requestAnimationFrame(drawFalling);
      } else {
        dotElm.remove();
      }
    }

    requestAnimationFrame(drawFalling);

    this.canvasElm.appendChild(dotElm);
  }
}
