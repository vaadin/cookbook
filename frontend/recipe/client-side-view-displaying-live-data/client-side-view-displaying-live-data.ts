import Tag from "../../generated/com/vaadin/recipes/recipe/Tag";
import { recipeInfo, Recipe } from "../recipe";
import { customElement, html, internalProperty } from "lit-element";

import "@vaadin/vaadin-charts";

@recipeInfo({
  url: "client-side-view-displaying-live-data",
  howDoI: "Show real-time updating content in a client-side view",
  description:
    "Learn how to automatically update the UI with data from a reactive backend.",
  sourceFiles: [
    "com/vaadin/recipes/recipe/clientsidelivedata/StockDataEndpoint.java",
  ],
  tags: [Tag.PUSH],
})
@customElement("client-side-view-displaying-live-data")
export class ShortcutListener extends Recipe {
  @internalProperty()
  private ticker = "FOO";
  @internalProperty()
  private currentPrice = "";
  @internalProperty()
  private priceHistory: number[] = [];

  render() {
    return html`
      <h2>${this.ticker} - $${this.currentPrice}</h2>
      <p>
        The backend in this demo sends 30 data points.
        <a href="/show-real-time-updating-data"
          >See this example using the server-side Java API.</a
        >
      </p>

      <vaadin-chart>
        <vaadin-chart-series
          title=${this.ticker}
          .values=${this.priceHistory}
        ></vaadin-chart-series>
      </vaadin-chart>
    `;
  }

  connectedCallback() {
    super.connectedCallback();

    // Connect to the backend
    const priceSource = new EventSource(
      `${window.location.origin}/stock-price`
    );

    // Listen for incoming data and update state
    priceSource.addEventListener("message", (e) => {
      this.currentPrice = JSON.parse(e.data);
      this.priceHistory = [...this.priceHistory, parseFloat(this.currentPrice)];
    });

    // The backend only sends 30 points, close after that
    priceSource.addEventListener("error", () => priceSource.close());
  }
}
