import { Container, Typography, Button, Box, Card, CardContent } from "@mui/material";

const Home = () => {
    return (
        <Container maxWidth="md" sx={{ mt: 5 }}>
      {/* Título principal */}
      <Typography variant="h3" align="center" gutterBottom>
        Bienvenido a mi Página 🚀
      </Typography>

      {/* Subtítulo */}
      <Typography variant="h6" align="center" color="text.secondary" gutterBottom>
        Esta es la página de inicio hecha con React y Material UI
      </Typography>

      {/* Tarjeta de ejemplo */}
      <Card sx={{ mt: 4, p: 2 }}>
        <CardContent>
          <Typography variant="h5" gutterBottom>
            Sección principal
          </Typography>
          <Typography variant="body1">
            Aquí puedes mostrar información destacada, noticias o lo que quieras.
          </Typography>
          <Box sx={{ mt: 2 }}>
            <Button variant="contained" color="primary">
              Acción principal
            </Button>
            <Button variant="outlined" sx={{ ml: 2 }}>
              Acción secundaria
            </Button>
          </Box>
        </CardContent>
      </Card>
    </Container>
    );
};

export default Home;