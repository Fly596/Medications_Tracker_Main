
/*
@Composable
fun TestScreen(
    viewModel: TestVM = hiltViewModel(),
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Test Screen", style = MaterialTheme.typography.displaySmall) }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
        ) {

            Button(
                onClick = viewModel::testNotification,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
            ) {
                Text("Test Notification")
            }
        }
    }
}   */
