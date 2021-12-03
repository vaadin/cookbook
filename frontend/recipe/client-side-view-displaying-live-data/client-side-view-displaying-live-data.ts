import "@vaadin/charts";
import "@vaadin/charts/src/vaadin-chart-series";
import { html } from "lit";
import { customElement, state } from "lit/decorators.js";
import Tag from "../../generated/com/vaadin/recipes/recipe/Tag";
import { Recipe, recipeInfo } from "../recipe";

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
export class LiveDataView extends Recipe {
  @state()
  private ticker = "FOO";
  @state()
  private currentPrice = "";
  @state()
  private priceHistory: number[][] = [];

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

      // The price history array contains pairs of [x,y] values
      this.priceHistory = [
        ...this.priceHistory,
        [this.priceHistory.length, parseFloat(this.currentPrice)],
      ];
    });

    // The backend only sends 30 points, close after that
    priceSource.addEventListener("error", () => priceSource.close());
  }
}
