export class Retry {
  constructor(private retries = 0) {
  }

  reset() {
    this.retries = 0;
  }

  getDelay(): number {
    this.retries++;
    return Math.pow(2, this.retries) * 1000;
  }
}
