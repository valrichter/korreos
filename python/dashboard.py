import dash
from dash import dcc, html
import plotly.graph_objs as go
from dash.dependencies import Input, Output
import pandas as pd
from shared_var import package_and_rider_records
import konsumer

# Iniciar el consumidor de Kafka
konsumer

# Datos compartidos
df = pd.DataFrame(package_and_rider_records)

app = dash.Dash(__name__, assets_folder='assets')

app.layout = html.Div([
    html.H1("Dashboard Packages Status", style={'textAlign': 'center'}),
    # Actualiza cada segundo
    dcc.Interval(id='interval-component', interval=1000, n_intervals=0),

    html.Div([
        html.H2("Package Status Table"),
        html.Table([
            html.Thead(html.Tr([
                html.Th("ID"), 
                html.Th("Rider"), 
                html.Th("Previous WH"),
                html.Th("Actual WH"),
                html.Th("Destiny WH"),
                html.Th("Status"), 
                html.Th("ETA Destiny WH")
            ])),
                html.Tbody(id='table-body')
        ], style={'width': '100%', 'border': '1px solid black', 'borderCollapse': 'collapse'}),
    ], style={'width': '50%', 'display': 'inline-block', 'verticalAlign': 'top'}),

    html.Div([
        dcc.Graph(id='status-graph')
    ], style={'width': '45%', 'display': 'inline-block', 'padding': '0 20px'})
])

@app.callback(
    [Output('table-body', 'children'),
     Output('status-graph', 'figure')],
    [Input('interval-component', 'n_intervals')]
)
def update_dashboard(n_intervals):
    df = pd.DataFrame(package_and_rider_records)
    print(f"Update dashboard: {package_and_rider_records}")
    table_rows = []
    for _, row in df.iterrows():
        table_rows.append(html.Tr([
            html.Td(row['package_id']),
            html.Td(row['rider_name']),
            html.Td(row['previous_warehouse']),
            html.Td(row['actual_warehouse']),
            html.Td(row['destiny_warehouse']),
            html.Td(row['status']),
            html.Td(row['destiny_eta'])
        ], style={'border': '1px solid black', 'padding': '8px', 'textAlign': 'left'}))

    status_counts = df['status'].value_counts()

    figure = {
        'data': [
            go.Bar(
                x=status_counts.index,
                y=status_counts.values,
                name='Number of Packages'
            )
        ],
        'layout': go.Layout(
            title='Number of Packages by Status',
            xaxis={'title': 'Status'},
            yaxis={'title': 'Number of Packages'}
        )
    }

    return table_rows, figure

if __name__ == '__main__':
    app.run_server(debug=False, threaded=True, port=8050, use_reloader=False)